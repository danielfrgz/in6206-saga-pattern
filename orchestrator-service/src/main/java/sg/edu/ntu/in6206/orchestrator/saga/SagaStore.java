package sg.edu.ntu.in6206.orchestrator.saga;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SagaStore {

    private final Map<String, SagaState> sagas = new ConcurrentHashMap<>();

    public void save(SagaState state) {
        sagas.put(state.getSagaId(), state);
    }

    public Optional<SagaState> find(String sagaId) {
        return Optional.ofNullable(sagas.get(sagaId));
    }

    public List<SagaState> findAll() {
        return List.copyOf(sagas.values());
    }
}
