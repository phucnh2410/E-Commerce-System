package com.spring.ecommercesystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "orders")
public class Order {

    public enum Status{
        preparing,
        delivering,
        received
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ordered_date")
    private Date orderedDate;

    @Column(name = "delivered_date")
    private Date deliveredDate;

    @Column(name = "received_date")
    private Date receivedDate;

    @Column(name = "status")
    private Status status;

    @Column(name = "total_amount")
    private Double totalAmount;

    //One to many
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Vourcher> vourchers;

    //many to One
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    //One to one
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH})
    private PaymentMethod paymentMethod;

    //Many to many
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinTable(
            name = "order_detail",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;


}

