package sg.edu.ntu.in6206.inventory.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sg.edu.ntu.in6206.inventory.domain.InventoryItem;
import sg.edu.ntu.in6206.inventory.repository.InventoryRepository;

@Component
public class InventorySeed implements CommandLineRunner {

    private final InventoryRepository repository;

    public InventorySeed(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        repository.save(new InventoryItem("SKU-LAPTOP", 5));
        repository.save(new InventoryItem("SKU-MOUSE", 100));
    }
}