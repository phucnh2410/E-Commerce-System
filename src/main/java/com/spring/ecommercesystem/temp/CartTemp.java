package com.spring.ecommercesystem.temp;

import com.spring.ecommercesystem.entities.Product;

public class CartTemp{
    private Product product;

    private int quantity;

    private Double total;

    public CartTemp() {
    }

    public CartTemp(Product product, int quantity, Double total) {
        this.product = product;
        this.quantity = quantity;
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public CartTemp setProduct(Product product) {
        this.product = product;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartTemp setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Double getTotal() {
        return total;
    }

    public CartTemp setTotal(Double total) {
        this.total = total;
        return this;
    }

    @Override
    public String toString() {
        return "CartTemp{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}