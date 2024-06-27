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
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_is_reduced")
    private Double priceIsReduced;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

//    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
//            CascadeType.REFRESH})
//    private List<Product> products;
}
