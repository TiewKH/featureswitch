package com.featureswitch.featureswitch.service.feature;

import com.featureswitch.featureswitch.entity.FeatureEntity;

public interface FeatureService {
    FeatureEntity getFeatureByName(String featureName);
}
