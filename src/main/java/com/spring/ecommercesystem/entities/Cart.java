package com.spring.ecommercesystem.entities;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private static final long serialVersionUID = 6993411625743306344L; //The unique version ID used in Java serialization to identify the version of a class
    private Product product;

    private String sellerName;

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

    public Cart setProduct(Product product) {
        this.product = product;
        return this;
    }

    public String getSellerName() {
        return sellerName;
    }

    public Cart setSellerName(String sellerName) {
        this.sellerName = sellerName;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Cart setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "product=" + (product != null ? product.getId() : "null") +
                ", quantity=" + quantity +
                '}';
    }
}
