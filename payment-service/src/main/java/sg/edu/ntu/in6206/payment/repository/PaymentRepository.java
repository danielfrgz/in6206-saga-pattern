package sg.edu.ntu.in6206.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.ntu.in6206.payment.domain.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrderId(String orderId);
}