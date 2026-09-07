package sg.edu.ntu.in6206.orchestrator.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sg.edu.ntu.in6206.orchestrator.saga.SagaOrchestrator;
import sg.edu.ntu.in6206.orchestrator.saga.SagaState;
import sg.edu.ntu.in6206.orchestrator.saga.SagaStore;
import sg.edu.ntu.in6206.orchestrator.web.dto.PlaceOrderRequest;

import java.util.List;

@RestController
@RequestMapping("/saga")
public class SagaController {

    private final SagaOrchestrator orchestrator;
    private final SagaStore store;

    public SagaController(SagaOrchestrator orchestrator, SagaStore store) {
        this.orchestrator = orchestrator;
        this.store = store;
    }

    @PostMapping("/orders")
    public SagaState placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return orchestrator.placeOrder(request);
    }

    @GetMapping("/{sagaId}")
    public SagaState findById(@PathVariable String sagaId) {
        return store.find(sagaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<SagaState> findAll() {
        return store.findAll();
    }
}
