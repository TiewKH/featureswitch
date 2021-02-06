package com.featureswitch.featureswitch.repository;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<FeatureEntity, Long> {
    FeatureEntity findByFeatureName(String featureName);
}
