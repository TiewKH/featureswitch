package com.featureswitch.featureswitch.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="USER_FEATURE")
public class UserFeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "FEATURE_ID")
    private FeatureEntity feature;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFeatureEntity that = (UserFeatureEntity) o;
        return id == that.id && user.equals(that.user) && feature.equals(that.feature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserFeatureEntity{" +
                "id=" + id +
                ", user=" + user.getEmail() +
                ", feature=" + feature.getFeatureName() +
                '}';
    }
}
