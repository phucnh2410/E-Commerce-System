package com.spring.ecommercesystem.dto;

import java.sql.Date;

public class ProductDTO {

    private Long id;
    private String name;
    private String img;

    private Double price;

    private Long orderId;

    private String orderStatus;

    private Date orderedDate;

    private int qtySold;

    private String customerName;

    public ProductDTO() {
    }

    public Long getId() {
        return id;
    }

    public ProductDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getImg() {
        return img;
    }

    public ProductDTO setImg(String img) {
        this.img = img;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public ProductDTO setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Long getOrderId() {
        return orderId;
    }

    public ProductDTO setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public ProductDTO setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public ProductDTO setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
        return this;
    }

    public int getQtySold() {
        return qtySold;
    }

    public ProductDTO setQtySold(int qtySold) {
        this.qtySold = qtySold;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ProductDTO setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }


    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", price=" + price +
                ", orderId=" + orderId +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderedDate=" + orderedDate +
                ", qtySold=" + qtySold +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
