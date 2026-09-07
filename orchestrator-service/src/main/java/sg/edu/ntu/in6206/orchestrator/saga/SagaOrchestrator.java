package sg.edu.ntu.in6206.orchestrator.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sg.edu.ntu.in6206.orchestrator.client.InventoryClient;
import sg.edu.ntu.in6206.orchestrator.client.PaymentClient;
import sg.edu.ntu.in6206.orchestrator.client.dto.CreateOrderRequest;
import sg.edu.ntu.in6206.orchestrator.client.OrderClient;
import sg.edu.ntu.in6206.orchestrator.web.dto.PlaceOrderRequest;

import java.util.UUID;

@Service
public class SagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(SagaOrchestrator.class);

    private final OrderClient orderClient;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;
    private final SagaStore store;
    private final long stepDelayMs;

    public SagaOrchestrator(OrderClient orderClient, PaymentClient paymentClient,
                            InventoryClient inventoryClient, SagaStore store,
                            @Value("${saga.step-delay-ms:0}") long stepDelayMs) {
        this.orderClient = orderClient;
        this.paymentClient = paymentClient;
        this.inventoryClient = inventoryClient;
        this.store = store;
        this.stepDelayMs = stepDelayMs;
    }

    public SagaState placeOrder(PlaceOrderRequest request) {
        String sagaId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("sagaId", sagaId);

        SagaState state = new SagaState(sagaId);
        store.save(state);

        UUID orderId = null;
        boolean paymentCharged = false;
        boolean stockReserved = false;

        try {
            log.info("SAGA START   sku={} qty={} amount={}",
                    request.sku(), request.quantity(), request.amount());

            orderId = orderClient.create(sagaId, new CreateOrderRequest(
                    request.customerId(), request.sku(),
                    request.quantity(), request.amount()));
            state.orderId(orderId);
            state.record("createOrder", StepOutcome.SUCCESS, "order=" + orderId);
            log.info("STEP 1/4 OK  createOrder order={}", orderId);
            pause();

            paymentClient.charge(sagaId, orderId.toString(), request.amount());
            paymentCharged = true;
            state.record("chargePayment", StepOutcome.SUCCESS, "amount=" + request.amount());
            log.info("STEP 2/4 OK  chargePayment amount={}", request.amount());
            pause();

            inventoryClient.reserve(sagaId, request.sku(), request.quantity());
            stockReserved = true;
            state.record("reserveStock", StepOutcome.SUCCESS,
                    request.sku() + " x" + request.quantity());
            log.info("STEP 3/4 OK  reserveStock {} x{}", request.sku(), request.quantity());
            pause();

            orderClient.confirm(sagaId, orderId);
            state.record("confirmOrder", StepOutcome.SUCCESS, "order=" + orderId);
            log.info("STEP 4/4 OK  confirmOrder order={}", orderId);

            state.status(SagaStatus.COMPLETED);
            log.info("SAGA COMPLETED");

        } catch (StepFailedException ex) {
            state.record(ex.getStep(), StepOutcome.FAILED, ex.getMessage());
            log.warn("STEP FAILED  {} — {}", ex.getStep(), ex.getMessage());
            compensate(sagaId, state, request, orderId, paymentCharged, stockReserved);
        } finally {
            MDC.clear();
        }

        return state;
    }

    private void compensate(String sagaId, SagaState state, PlaceOrderRequest request,
                            UUID orderId, boolean paymentCharged, boolean stockReserved) {

        state.status(SagaStatus.COMPENSATING);
        log.warn("SAGA COMPENSATING — unwinding completed steps in reverse");

        if (stockReserved) {
            compensateStep(state, "reserveStock", () ->
                    inventoryClient.release(sagaId, request.sku(), request.quantity()));
            pause();
        }
        if (paymentCharged) {
            compensateStep(state, "chargePayment", () ->
                    paymentClient.refund(sagaId, orderId.toString()));
            pause();
        }
        if (orderId != null) {
            compensateStep(state, "createOrder", () -> orderClient.cancel(sagaId, orderId));
        }

        state.status(SagaStatus.COMPENSATED);
        log.warn("SAGA COMPENSATED");
    }

    private void compensateStep(SagaState state, String step, Runnable action) {
        try {
            action.run();
            state.record(step, StepOutcome.COMPENSATED, "compensated");
            log.warn("COMPENSATE   {} OK", step);
        } catch (Exception ex) {
            state.record(step, StepOutcome.COMPENSATION_FAILED, ex.getMessage());
            log.error("COMPENSATION FAILED {} — requires manual intervention", step, ex);
        }
    }

    private void pause() {
        if (stepDelayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(stepDelayMs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}


