package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "categories")
public class Category implements Serializable {
    private static final long serialVersionUID = 5942615693414973512L;
    public enum Status{
        Reject,
        Approved,
        Pending
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;


    @OneToMany(mappedBy = "category", cascade = CascadeType.REFRESH)
    @JsonBackReference("category-products")
//    @JsonIgnoreProperties({"category"})
    private List<Product> products;
}
