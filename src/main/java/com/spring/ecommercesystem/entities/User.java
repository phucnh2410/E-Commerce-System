package com.spring.ecommercesystem.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "users")
public class User {
    @Transient
    public String getAvatarPath(){
        if (this.id == null || this.avatar == null) return null;
        return "/userAvatar/" + this.id + "/" + this.avatar;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar")
    private String avatar;

    //Many to one
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "role_id")
    private Role role;

    //One to many
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonManagedReference("user-addresses")
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonManagedReference("user-products")
    private List<Product> products;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonManagedReference("user-feedbacks")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonManagedReference("user-orders")
    private List<Order> orders;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonManagedReference("user-deliveryInfos")
    private List<DeliveryInfo> deliveryInfos;

}
