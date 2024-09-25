package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "vouchers")
public class Voucher {

    public enum CustomerType{
        NEW,
        SILVER,
        GOLD,
        DIAMOND,
        VIP
    }

    public enum Status{
        New,
        Published,
        Expired,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "percentage_is_reduced")
    private int percentageIsReduced;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "start_date")
    private Date startDate;

//    public Voucher setDeadline(int daysToAdd) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 3);
//        this.deadline = (Date) calendar.getTime();
//        return this;
//    }

    //One to many
    @OneToMany(mappedBy = "voucher", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonBackReference("voucher-orders")
    private List<Order> orders;

    @OneToMany(mappedBy = "voucher", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonManagedReference("voucher-vouchers_detail")
    private List<VoucherDetail> voucherDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return quantity == voucher.quantity && percentageIsReduced == voucher.percentageIsReduced && Objects.equals(id, voucher.id) && Objects.equals(description, voucher.description) && customerType == voucher.customerType && status == voucher.status && Objects.equals(expiryDate, voucher.expiryDate) && Objects.equals(startDate, voucher.startDate) && Objects.equals(orders, voucher.orders) && Objects.equals(voucherDetails, voucher.voucherDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, description, customerType, status, percentageIsReduced, expiryDate, startDate, orders, voucherDetails);
    }
}
