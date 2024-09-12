package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "products")
public class Product implements Serializable {
    private static final long serialVersionUID = 3032430709415055160L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "stock")
    private int stock;

    @Column(name = "brand")
    private String brand;

    @Column(name = "product_image")
    private String productImg;

    @Column(name = "created_at")
    private Date createAt;

    // Many to One
//    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "category_id")
//    @JsonManagedReference("category-products")
    @JsonBackReference("category-products")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-products")
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
//            CascadeType.REFRESH})
//    @JoinColumn(name = "discount_id")
//    private Discount discount;

    //One to many
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonManagedReference("product-feedbacks")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("product-order_details")
    private List<OrderDetail> orderDetails;



    public double getAverageFeedback(){
        OptionalDouble average = feedbacks.stream().mapToDouble(Feedback::getFeedbackRating).average();
        double finalAverage = average.orElse(0.0);

        return finalAverage;
    }

    public int getNumberOfFeedbacks(){
        return feedbacks.size();
    }

}
