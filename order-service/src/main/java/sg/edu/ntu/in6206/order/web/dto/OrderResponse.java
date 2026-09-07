package sg.edu.ntu.in6206.order.web.dto;

import sg.edu.ntu.in6206.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(UUID orderId, String customerId, String sku,
                            int quantity, BigDecimal amount, OrderStatus status) {}
