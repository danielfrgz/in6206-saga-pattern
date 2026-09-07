package sg.edu.ntu.in6206.inventory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InventoryItem {

    @Id
    private String sku;

    private int available;
    private int reserved;

    protected InventoryItem() {
    }

    public InventoryItem(String sku, int available) {
        this.sku = sku;
        this.available = available;
        this.reserved = 0;
    }

    public void reserve(int quantity) {
        if (quantity > available) {
            throw new InsufficientStockException(sku, quantity, available);
        }
        available -= quantity;
        reserved += quantity;
    }

    public void release(int quantity) {
        int toRelease = Math.min(quantity, reserved);
        reserved -= toRelease;
        available += toRelease;
    }

    public String getSku() {
        return sku;
    }

    public int getAvailable() {
        return available;
    }

    public int getReserved() {
        return reserved;
    }
}
