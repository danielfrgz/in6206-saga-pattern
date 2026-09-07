package sg.edu.ntu.in6206.payment.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String orderId;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    protected Payment() {
    }

    public Payment(String orderId, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.CHARGED;
    }

    public void refund() {
        if (status == PaymentStatus.REFUNDED) {
            return;
        }
        this.status = PaymentStatus.REFUNDED;
    }

    public UUID getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}