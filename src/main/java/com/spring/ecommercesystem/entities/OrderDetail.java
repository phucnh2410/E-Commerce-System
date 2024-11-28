package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    public enum ProductStatus{
        Pending,
        Confirmed
    }

    @EmbeddedId
    private OrderDetailKey id; //set both orderId and productId are a primary key
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-order_details")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonBackReference("product-order_details")
    private Product product;

    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Column(name = "product_quantity")
    private int productQuantity;

    public OrderDetail() {
    }

    public OrderDetail(OrderDetailKey id, Order order, Product product, int productQuantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.productQuantity = productQuantity;
    }

    public OrderDetailKey getId() {
        return id;
    }

    public OrderDetail setId(OrderDetailKey id) {
        this.id = id;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public OrderDetail setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public OrderDetail setProduct(Product product) {
        this.product = product;
        return this;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public OrderDetail setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
        return this;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public OrderDetail setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
        return this;
    }
}
