package sg.edu.ntu.in6206.orchestrator.saga;

public class StepFailedException extends RuntimeException {

    private final String step;

    public StepFailedException(String step, String detail) {
        super(detail);
        this.step = step;
    }

    public String getStep() {
        return step;
    }
}
