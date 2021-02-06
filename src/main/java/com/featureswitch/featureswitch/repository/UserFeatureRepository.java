package com.featureswitch.featureswitch.repository;

import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeatureRepository extends JpaRepository<UserFeatureEntity, Long> {
    UserFeatureEntity findByUserIdAndFeatureId(long userId, long featureId);
}
