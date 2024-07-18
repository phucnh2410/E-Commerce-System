package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
        pending_confirmation,
        confirmed,
        preparing,
        delivering,
        received,
        canceled
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


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "address_id")
    @JsonBackReference("address-orders")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "voucher_id")
    @JsonBackReference("voucher-orders")
    private Voucher voucher;

    //many to One
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-orders")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "paymentMethod_id")
    @JsonBackReference("payment-order")
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

