package sg.edu.ntu.in6206.inventory.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sg.edu.ntu.in6206.inventory.service.InventoryService;
import sg.edu.ntu.in6206.inventory.web.dto.StockRequest;
import sg.edu.ntu.in6206.inventory.web.dto.StockResponse;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping("/reserve")
    public StockResponse reserve(@Valid @RequestBody StockRequest request) {
        return service.reserve(request.sku(), request.quantity());
    }

    @PostMapping("/release")
    public StockResponse release(@Valid @RequestBody StockRequest request) {
        return service.release(request.sku(), request.quantity());
    }

    @GetMapping("/{sku}")
    public StockResponse find(@PathVariable String sku) {
        return service.find(sku);
    }
}