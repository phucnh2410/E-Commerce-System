package com.spring.ecommercesystem.temp;

import com.spring.ecommercesystem.entities.Address;
import com.spring.ecommercesystem.entities.PaymentMethod;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.entities.Voucher;

import java.util.List;

public class OrderTemp {
    private List<UserCart> userCarts;

    private Voucher voucher;

    private PaymentMethod paymentMethod;

    private Address address;

    private Double finalTotal;

    public OrderTemp() {
    }

    public OrderTemp(List<UserCart> userCarts, Voucher voucher, PaymentMethod paymentMethod, Address address, Double finalTotal) {
        this.userCarts = userCarts;
        this.voucher = voucher;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.finalTotal = finalTotal;
    }

    public List<UserCart> getUserCarts() {
        return userCarts;
    }

    public OrderTemp setUserCarts(List<UserCart> userCarts) {
        this.userCarts = userCarts;
        return this;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public OrderTemp setVoucher(Voucher voucher) {
        this.voucher = voucher;
        return this;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public OrderTemp setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public OrderTemp setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Double getFinalTotal() {
        return finalTotal;
    }

    public OrderTemp setFinalTotal(Double finalTotal) {
        this.finalTotal = finalTotal;
        return this;
    }
}
