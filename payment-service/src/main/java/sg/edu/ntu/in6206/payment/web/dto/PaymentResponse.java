package sg.edu.ntu.in6206.payment.web.dto;

import sg.edu.ntu.in6206.payment.domain.PaymentStatus;
import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(UUID paymentId, String orderId,
                              BigDecimal amount, PaymentStatus status) {}
