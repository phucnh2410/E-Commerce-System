package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "feedback_date")
    private Date feedbackDate;

    @Column(name = "feedback_rating")
    private Double feedbackRating;


    //Many to One
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-feedbacks")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-feedbacks")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "product_id")
    @JsonBackReference("product-feedbacks")
    private Product product;


}
