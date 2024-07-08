package com.spring.ecommercesystem.entities;

import java.io.Serializable;

public class Cart implements Serializable {
    private Product product;

    private int quantity;

    public double getTotalPerProduct(){
        if (product != null) {
            return product.getPrice() * quantity;
        }
        return 0.0;
    }
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
