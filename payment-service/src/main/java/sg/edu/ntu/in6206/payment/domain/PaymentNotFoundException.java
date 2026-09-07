package sg.edu.ntu.in6206.payment.domain;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String orderId) {
        super("No payment found for order %s".formatted(orderId));
    }
}