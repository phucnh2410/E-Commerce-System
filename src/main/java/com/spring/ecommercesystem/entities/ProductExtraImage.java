package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "product_extra_image")
public class ProductExtraImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_extra_img_src")
    private String productExtraImgSrc;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "product_id")
    @JsonBackReference("product-productExtraImages")
    private Product product;


    public ProductExtraImage() {
    }

    public Long getId() {
        return id;
    }

    public ProductExtraImage setId(Long id) {
        this.id = id;
        return this;
    }

    public String getProductExtraImgSrc() {
        return productExtraImgSrc;
    }

    public ProductExtraImage setProductExtraImgSrc(String productExtraImgSrc) {
        this.productExtraImgSrc = productExtraImgSrc;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public ProductExtraImage setProduct(Product product) {
        this.product = product;
        return this;
    }

    @Override
    public String toString() {
        return "ProductExtraImage{" +
                "id=" + id +
                ", productExtraImgSrc='" + productExtraImgSrc + '\'' +
                ", product=" + product +
                '}';
    }
}
