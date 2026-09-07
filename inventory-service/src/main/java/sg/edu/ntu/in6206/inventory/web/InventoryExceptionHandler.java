package sg.edu.ntu.in6206.inventory.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sg.edu.ntu.in6206.inventory.domain.InsufficientStockException;
import sg.edu.ntu.in6206.inventory.domain.ItemNotFoundException;

import java.util.Map;

@RestControllerAdvice
class InventoryExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    ResponseEntity<Map<String, String>> handleInsufficientStock(InsufficientStockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    ResponseEntity<Map<String, String>> handleItemNotFound(ItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

}