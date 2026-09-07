package sg.edu.ntu.in6206.payment.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ChargeRequest(@NotBlank String orderId,
                            @NotNull @Positive BigDecimal amount) {}
