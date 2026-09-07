package sg.edu.ntu.in6206.order.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sg.edu.ntu.in6206.order.service.OrderService;
import sg.edu.ntu.in6206.order.web.dto.CreateOrderRequest;
import sg.edu.ntu.in6206.order.web.dto.OrderResponse;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return service.create(request);
    }

    @PostMapping("/{orderId}/confirm")
    public OrderResponse confirm(@PathVariable UUID orderId) {
        return service.confirm(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancel(@PathVariable UUID orderId) {
        return service.cancel(orderId);
    }

    @GetMapping("/{orderId}")
    public OrderResponse findById(@PathVariable UUID orderId) {
        return service.findById(orderId);
    }
}
