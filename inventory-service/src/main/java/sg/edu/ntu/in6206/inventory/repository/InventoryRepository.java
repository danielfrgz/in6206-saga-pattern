package sg.edu.ntu.in6206.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.ntu.in6206.inventory.domain.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, String> {}