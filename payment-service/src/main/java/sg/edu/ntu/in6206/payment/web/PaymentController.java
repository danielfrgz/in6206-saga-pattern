package sg.edu.ntu.in6206.payment.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sg.edu.ntu.in6206.payment.service.PaymentService;
import sg.edu.ntu.in6206.payment.web.dto.ChargeRequest;
import sg.edu.ntu.in6206.payment.web.dto.PaymentResponse;
import sg.edu.ntu.in6206.payment.web.dto.RefundRequest;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/charge")
    public PaymentResponse charge(@Valid @RequestBody ChargeRequest request) {
        return service.charge(request.orderId(), request.amount());
    }

    @PostMapping("/refund")
    public PaymentResponse refund(@Valid @RequestBody RefundRequest request) {
        return service.refund(request.orderId());
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse findByOrder(@PathVariable String orderId) {
        return service.findByOrder(orderId);
    }
}
