package sg.edu.ntu.in6206.orchestrator.saga;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class SagaState {

    private final String sagaId;
    private final Instant startedAt = Instant.now();
    private final List<StepRecord> steps = new CopyOnWriteArrayList<>();

    private SagaStatus status = SagaStatus.STARTED;
    private UUID orderId;

    public SagaState(String sagaId) {
        this.sagaId = sagaId;
    }

    public void record(String step, StepOutcome outcome, String detail) {
        steps.add(StepRecord.of(step, outcome, detail));
    }

    public void status(SagaStatus status) {
        this.status = status;
    }

    public void orderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getSagaId() {
        return sagaId;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public List<StepRecord> getSteps() {
        return steps;
    }

    public SagaStatus getStatus() {
        return status;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
