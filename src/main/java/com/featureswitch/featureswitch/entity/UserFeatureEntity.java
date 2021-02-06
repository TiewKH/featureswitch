package com.featureswitch.featureswitch.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="USER_FEATURE")
public class UserFeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "FEATURE_ID")
    private long featureId;
}
