package com.spring.ecommercesystem.temp;

import com.spring.ecommercesystem.entities.Product;

public class CartTemp{
    private Product product;

    private int quantity;

    public CartTemp() {
    }

    public CartTemp(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return "CartTemp{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}