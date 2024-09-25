package com.spring.ecommercesystem.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "vouchers_detail")
public class VoucherDetail {
    public enum Status{
        Unused,
        Used,
        Expired
    }

    @EmbeddedId
    private VoucherDetailKey id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-vouchers_detail")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @MapsId("voucherId")
    @JoinColumn(name = "voucher_id")
    @JsonBackReference("voucher-vouchers_detail")
    private Voucher voucher;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;


    public VoucherDetail() {
    }

    public VoucherDetail(VoucherDetailKey id, User user, Voucher voucher, Status status) {
        this.id = id;
        this.user = user;
        this.voucher = voucher;
        this.status = status;
    }

    public VoucherDetailKey getId() {
        return id;
    }

    public VoucherDetail setId(VoucherDetailKey id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public VoucherDetail setUser(User user) {
        this.user = user;
        return this;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public VoucherDetail setVoucher(Voucher voucher) {
        this.voucher = voucher;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public VoucherDetail setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "VoucherDetail{" +
                "id=" + id +
                ", user=" + user +
                ", voucher=" + voucher +
                ", status=" + status +
                '}';
    }
}
