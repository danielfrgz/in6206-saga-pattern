package sg.edu.ntu.in6206.orchestrator.client.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(String customerId, String sku,
                                 int quantity, BigDecimal amount) {}
