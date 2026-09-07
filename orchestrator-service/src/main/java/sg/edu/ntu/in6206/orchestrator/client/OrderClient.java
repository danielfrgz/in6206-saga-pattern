package sg.edu.ntu.in6206.orchestrator.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import sg.edu.ntu.in6206.orchestrator.client.dto.CreateOrderRequest;
import sg.edu.ntu.in6206.orchestrator.client.dto.CreatedOrder;
import sg.edu.ntu.in6206.orchestrator.config.SagaIdFilter;
import sg.edu.ntu.in6206.orchestrator.saga.StepFailedException;

import java.util.UUID;

@Component
public class OrderClient {

    private final RestClient client;

    public OrderClient(RestClient.Builder builder,
                       @Value("${services.order.url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public UUID create(String sagaId, CreateOrderRequest request) {
        CreatedOrder created = client.post()
                .uri("/orders")
                .header(SagaIdFilter.HEADER, sagaId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new StepFailedException("createOrder", ErrorBody.body(res));
                })
                .body(CreatedOrder.class);
        return created.orderId();
    }

    public void confirm(String sagaId, UUID orderId) {
        client.post()
                .uri("/orders/{orderId}/confirm", orderId)
                .header(SagaIdFilter.HEADER, sagaId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new StepFailedException("confirmOrder", ErrorBody.body(res));
                })
                .toBodilessEntity();
    }

    public void cancel(String sagaId, UUID orderId) {
        client.post()
                .uri("/orders/{orderId}/cancel", orderId)
                .header(SagaIdFilter.HEADER, sagaId)
                .retrieve()
                .toBodilessEntity();
    }
}