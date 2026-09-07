package sg.edu.ntu.in6206.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.ntu.in6206.order.domain.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
