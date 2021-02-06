package com.featureswitch.featureswitch.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="FEATURE")
public class FeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEATURE_ID")
    private long featureId;

    @Column(name = "FEATURE_NAME", unique = true)
    private String featureName;
}
