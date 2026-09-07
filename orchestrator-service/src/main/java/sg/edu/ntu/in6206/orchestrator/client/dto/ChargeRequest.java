package sg.edu.ntu.in6206.orchestrator.client.dto;

import java.math.BigDecimal;

public record ChargeRequest(String orderId, BigDecimal amount) {}
