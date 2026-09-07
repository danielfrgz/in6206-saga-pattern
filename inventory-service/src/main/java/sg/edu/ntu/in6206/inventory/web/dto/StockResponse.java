package sg.edu.ntu.in6206.inventory.web.dto;

public record StockResponse(String sku, int available, int reserved) {}
