package sg.edu.ntu.in6206.inventory.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record StockRequest(@NotBlank String sku, @Positive int quantity) {}