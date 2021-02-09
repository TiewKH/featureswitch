package com.featureswitch.featureswitch.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="USER")
public class UserEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    Set<UserFeatureEntity> permissions = new HashSet<UserFeatureEntity>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return userId == that.userId && email.equals(that.email) && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
