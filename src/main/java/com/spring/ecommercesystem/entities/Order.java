package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

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

    @Column(name = "confirmed_date")
    private Date confirmedDate;

    @Column(name = "canceled_date")
    private Date canceledDate;

    @Column(name = "delivered_date")
    private Date deliveredDate;

    @Column(name = "received_date")
    private Date receivedDate;

    @Column(name = "status")
    private Status status;

    @Column(name = "total_amount")
    private Double totalAmount;

    //One to many
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH}, orphanRemoval = true)
    @JsonManagedReference("order-order_details")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JsonManagedReference("order-feedbacks")
    private List<Feedback> feedbacks;

    //many to One
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "address_id")
    @JsonBackReference("address-orders")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "voucher_id")
    @JsonBackReference("voucher-orders")
    private Voucher voucher;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-orders")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "paymentMethod_id")
    @JsonBackReference("payment-orders")
    private PaymentMethod paymentMethod;


    public Order(Long id, Date orderedDate, Date confirmedDate, Date canceledDate, Date deliveredDate, Date receivedDate, Status status, Double totalAmount, List<OrderDetail> orderDetails, Address address, Voucher voucher, User user, PaymentMethod paymentMethod) {
        this.id = id;
        this.orderedDate = orderedDate;
        this.confirmedDate = confirmedDate;
        this.canceledDate = canceledDate;
        this.deliveredDate = deliveredDate;
        this.receivedDate = receivedDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.address = address;
        this.voucher = voucher;
        this.user = user;
        this.paymentMethod = paymentMethod;
    }

    public Order(Long id, Date orderedDate, Date deliveredDate, Date receivedDate, Status status, Double totalAmount, List<OrderDetail> orderDetails, Address address, Voucher voucher, User user, PaymentMethod paymentMethod) {
        this.id = id;
        this.orderedDate = orderedDate;
        this.deliveredDate = deliveredDate;
        this.receivedDate = receivedDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.address = address;
        this.voucher = voucher;
        this.user = user;
        this.paymentMethod = paymentMethod;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public Order setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
        return this;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public Order setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
        return this;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public Order setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    public Date getConfirmedDate() {
        return confirmedDate;
    }

    public Order setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
        return this;
    }

    public Date getCanceledDate() {
        return canceledDate;
    }

    public Order setCanceledDate(Date canceledDate) {
        this.canceledDate = canceledDate;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Order setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Order setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public Order setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
        return this;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public Order setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Order setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public Order setVoucher(Voucher voucher) {
        this.voucher = voucher;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Order setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }
}

