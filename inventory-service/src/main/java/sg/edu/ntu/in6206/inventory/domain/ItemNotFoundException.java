package sg.edu.ntu.in6206.inventory.domain;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String sku) {
        super("No inventory item found for SKU %s".formatted(sku));
    }
}