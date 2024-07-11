//package com.spring.ecommercesystem.dto;
//
//
//import java.io.Serializable;
//import java.util.List;
//
//public class ProductDTO implements Serializable{
//
//    private static final long serialVersionUID = 6993411625743306344L; //The unique version ID used in Java serialization to identify the version of a class
//
//    private Long id;
//
//    private String name;
//
//    private Double price;
//
//    private String productImg;
//
//    private String categoryName;
//
//    private String sellerName;
//
//    public ProductDTO() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public ProductDTO setId(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public ProductDTO setName(String name) {
//        this.name = name;
//        return this;
//    }
//
//    public Double getPrice() {
//        return price;
//    }
//
//    public ProductDTO setPrice(Double price) {
//        this.price = price;
//        return this;
//    }
//
//    public String getProductImg() {
//        return productImg;
//    }
//
//    public ProductDTO setProductImg(String productImg) {
//        this.productImg = productImg;
//        return this;
//    }
//
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public ProductDTO setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//        return this;
//    }
//
//    public String getSellerName() {
//        return sellerName;
//    }
//
//    public ProductDTO setSellerName(String sellerName) {
//        this.sellerName = sellerName;
//        return this;
//    }
//
//    @Override
//    public String toString() {
//        return "ProductDTO{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", price=" + price +
//                ", productImg='" + productImg + '\'' +
//                ", categoryName='" + categoryName + '\'' +
//                ", sellerName='" + sellerName + '\'' +
//                '}';
//    }
//}
