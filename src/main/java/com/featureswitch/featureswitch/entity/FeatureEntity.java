package com.featureswitch.featureswitch.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name="FEATURE")
public class FeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEATURE_ID")
    private long featureId;

    @Column(name = "FEATURE_NAME", unique = true)
    private String featureName;

    @OneToMany(mappedBy = "feature")
    Set<UserFeatureEntity> permissions = new HashSet<UserFeatureEntity>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureEntity that = (FeatureEntity) o;
        return featureId == that.featureId && featureName.equals(that.featureName) && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureId);
    }

    @Override
    public String toString() {
        return "FeatureEntity{" +
                "featureId=" + featureId +
                ", featureName='" + featureName + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
