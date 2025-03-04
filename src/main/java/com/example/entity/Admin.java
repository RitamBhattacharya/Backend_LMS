package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminID;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(length = 50)
    private String designation;

    @Column(length = 50)
    private String department;

    @Column(length = 1)
    private char gender;

    @Column(nullable = false, length = 255)
    private String password;
}
