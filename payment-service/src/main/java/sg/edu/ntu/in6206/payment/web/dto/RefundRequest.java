package sg.edu.ntu.in6206.payment.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RefundRequest(@NotBlank String orderId) {}
