package sg.edu.ntu.in6206.order.domain;

import java.util.UUID;

public class IllegalOrderTransitionException extends RuntimeException {

    public IllegalOrderTransitionException(UUID id, OrderStatus from, OrderStatus to) {
        super("Order %s cannot move from %s to %s".formatted(id, from, to));
    }
}
