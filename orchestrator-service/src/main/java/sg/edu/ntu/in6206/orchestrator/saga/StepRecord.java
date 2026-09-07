package sg.edu.ntu.in6206.orchestrator.saga;

import java.time.Instant;

public record StepRecord(String step, StepOutcome outcome, String detail, Instant at) {

    public static StepRecord of(String step, StepOutcome outcome, String detail) {
        return new StepRecord(step, outcome, detail, Instant.now());
    }
}
