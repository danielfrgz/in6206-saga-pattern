package sg.edu.ntu.in6206.inventory.service;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sg.edu.ntu.in6206.inventory.domain.InventoryItem;
import sg.edu.ntu.in6206.inventory.domain.ItemNotFoundException;
import sg.edu.ntu.in6206.inventory.repository.InventoryRepository;
import sg.edu.ntu.in6206.inventory.web.dto.StockResponse;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public StockResponse reserve(String sku, int quantity) {
        InventoryItem item = repository.findById(sku)
                .orElseThrow(() -> new ItemNotFoundException(sku));
        item.reserve(quantity);
        log.info("RESERVE  {} x{} -> available={}", sku, quantity, item.getAvailable());
        return toResponse(item);
    }

    @Transactional
    public StockResponse release(String sku, int quantity) {
        InventoryItem item = repository.findById(sku)
                .orElseThrow(() -> new ItemNotFoundException(sku));
        item.release(quantity);
        log.info("COMPENSATE release {} x{} -> available={}", sku, quantity, item.getAvailable());
        return toResponse(item);
    }

    @Transactional(readOnly = true)
    public StockResponse find(String sku) {
        InventoryItem item = repository.findById(sku)
                .orElseThrow(() -> new ItemNotFoundException(sku));
        return toResponse(item);
    }

    private StockResponse toResponse(InventoryItem item) {
        return new StockResponse(item.getSku(), item.getAvailable(), item.getReserved());
    }
}