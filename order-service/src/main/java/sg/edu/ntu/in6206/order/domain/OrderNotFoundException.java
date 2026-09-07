package sg.edu.ntu.in6206.order.domain;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID id) {
        super("No order found with id %s".formatted(id));
    }
}
