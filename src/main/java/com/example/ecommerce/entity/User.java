package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "USERS")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}

