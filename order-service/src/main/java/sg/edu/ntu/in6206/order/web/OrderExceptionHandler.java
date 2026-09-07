package sg.edu.ntu.in6206.order.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sg.edu.ntu.in6206.order.domain.IllegalOrderTransitionException;
import sg.edu.ntu.in6206.order.domain.OrderNotFoundException;

import java.util.Map;

@RestControllerAdvice
class OrderExceptionHandler {

    @ExceptionHandler(IllegalOrderTransitionException.class)
    ResponseEntity<Map<String, String>> handleIllegalTransition(IllegalOrderTransitionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}
