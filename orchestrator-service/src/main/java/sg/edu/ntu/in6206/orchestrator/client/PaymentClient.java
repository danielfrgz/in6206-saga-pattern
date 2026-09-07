package sg.edu.ntu.in6206.orchestrator.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import sg.edu.ntu.in6206.orchestrator.client.dto.ChargeRequest;
import sg.edu.ntu.in6206.orchestrator.client.dto.RefundRequest;
import sg.edu.ntu.in6206.orchestrator.config.SagaIdFilter;
import sg.edu.ntu.in6206.orchestrator.saga.StepFailedException;

import java.math.BigDecimal;

@Component
public class PaymentClient {

    private final RestClient client;

    public PaymentClient(RestClient.Builder builder,
                         @Value("${services.payment.url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public void charge(String sagaId, String orderId, BigDecimal amount) {
        client.post()
                .uri("/payments/charge")
                .header(SagaIdFilter.HEADER, sagaId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ChargeRequest(orderId, amount))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new StepFailedException("chargePayment", ErrorBody.body(res));
                })
                .toBodilessEntity();
    }

    public void refund(String sagaId, String orderId) {
        client.post()
                .uri("/payments/refund")
                .header(SagaIdFilter.HEADER, sagaId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RefundRequest(orderId))
                .retrieve()
                .toBodilessEntity();
    }
}
