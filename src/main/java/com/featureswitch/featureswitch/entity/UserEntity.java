package com.featureswitch.featureswitch.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="USER")
public class UserEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "EMAIL", unique = true)
    private String email;
}
