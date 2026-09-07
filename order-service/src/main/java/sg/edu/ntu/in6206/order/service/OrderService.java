package sg.edu.ntu.in6206.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.ntu.in6206.order.domain.Order;
import sg.edu.ntu.in6206.order.domain.OrderNotFoundException;
import sg.edu.ntu.in6206.order.repository.OrderRepository;
import sg.edu.ntu.in6206.order.web.dto.CreateOrderRequest;
import sg.edu.ntu.in6206.order.web.dto.OrderResponse;

import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Order order = repository.save(new Order(request.customerId(), request.sku(),
                request.quantity(), request.amount()));
        log.info("CREATE   order={} sku={} qty={} status={}",
                order.getId(), order.getSku(), order.getQuantity(), order.getStatus());
        return toResponse(order);
    }

    @Transactional
    public OrderResponse confirm(UUID orderId) {
        Order order = find(orderId);
        order.confirm();
        log.info("CONFIRM  order={} status={}", orderId, order.getStatus());
        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancel(UUID orderId) {
        Order order = find(orderId);
        order.cancel();
        log.info("COMPENSATE cancel order={} status={}", orderId, order.getStatus());
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(UUID orderId) {
        return toResponse(find(orderId));
    }

    private Order find(UUID orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getSku(),
                order.getQuantity(), order.getAmount(), order.getStatus());
    }
}
