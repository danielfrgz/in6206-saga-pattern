package sg.edu.ntu.in6206.orchestrator.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import sg.edu.ntu.in6206.orchestrator.client.dto.StockRequest;
import sg.edu.ntu.in6206.orchestrator.config.SagaIdFilter;
import sg.edu.ntu.in6206.orchestrator.saga.StepFailedException;

@Component
public class InventoryClient {

    private final RestClient client;

    public InventoryClient(RestClient.Builder builder,
                           @Value("${services.inventory.url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public void reserve(String sagaId, String sku, int quantity) {
        client.post()
                .uri("/inventory/reserve")
                .header(SagaIdFilter.HEADER, sagaId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new StockRequest(sku, quantity))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new StepFailedException("reserveStock", ErrorBody.body(res));
                })
                .toBodilessEntity();
    }

    public void release(String sagaId, String sku, int quantity) {
        client.post()
                .uri("/inventory/release")
                .header(SagaIdFilter.HEADER, sagaId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new StockRequest(sku, quantity))
                .retrieve()
                .toBodilessEntity();
    }
}
