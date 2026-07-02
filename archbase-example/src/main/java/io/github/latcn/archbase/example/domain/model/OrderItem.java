package io.github.latcn.archbase.example.domain.model;

import io.github.latcn.archbase.foundation.valueobject.IValueObject;

import java.math.BigDecimal;

public class OrderItem implements IValueObject {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;

    public OrderItem() {}

    public OrderItem(Long productId, Integer quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}