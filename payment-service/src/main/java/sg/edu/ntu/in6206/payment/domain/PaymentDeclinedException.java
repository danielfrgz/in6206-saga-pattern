package sg.edu.ntu.in6206.payment.domain;

import java.math.BigDecimal;

public class PaymentDeclinedException extends RuntimeException {

    public PaymentDeclinedException(BigDecimal amount, BigDecimal limit) {
        super("Payment declined: amount %s exceeds limit %s".formatted(amount, limit));
    }
}
