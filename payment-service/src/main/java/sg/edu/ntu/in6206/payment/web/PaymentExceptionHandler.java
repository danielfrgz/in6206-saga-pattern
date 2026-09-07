package sg.edu.ntu.in6206.payment.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sg.edu.ntu.in6206.payment.domain.PaymentDeclinedException;
import sg.edu.ntu.in6206.payment.domain.PaymentNotFoundException;

import java.util.Map;

@RestControllerAdvice
class PaymentExceptionHandler {

    @ExceptionHandler(PaymentDeclinedException.class)
    ResponseEntity<Map<String, String>> handlePaymentDeclined(PaymentDeclinedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    ResponseEntity<Map<String, String>> handlePaymentNotFound(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}