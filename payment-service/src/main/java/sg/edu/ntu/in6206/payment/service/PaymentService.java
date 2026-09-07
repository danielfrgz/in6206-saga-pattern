package sg.edu.ntu.in6206.payment.service;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sg.edu.ntu.in6206.payment.domain.Payment;
import sg.edu.ntu.in6206.payment.domain.PaymentDeclinedException;
import sg.edu.ntu.in6206.payment.domain.PaymentNotFoundException;
import sg.edu.ntu.in6206.payment.repository.PaymentRepository;
import sg.edu.ntu.in6206.payment.web.dto.PaymentResponse;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private static final BigDecimal CREDIT_LIMIT = new BigDecimal("10000");

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PaymentResponse charge(String orderId, BigDecimal amount) {
        if (amount.compareTo(CREDIT_LIMIT) > 0) {
            throw new PaymentDeclinedException(amount, CREDIT_LIMIT);
        }
        Payment payment = repository.save(new Payment(orderId, amount));
        log.info("CHARGE   order={} amount={} status={}", orderId, amount, payment.getStatus());
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse refund(String orderId) {
        Payment payment = repository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(orderId));
        payment.refund();
        log.info("COMPENSATE refund order={} status={}", orderId, payment.getStatus());
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse findByOrder(String orderId) {
        return repository.findByOrderId(orderId)
                .map(this::toResponse)
                .orElseThrow(() -> new PaymentNotFoundException(orderId));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getOrderId(),
                payment.getAmount(), payment.getStatus());
    }
}
