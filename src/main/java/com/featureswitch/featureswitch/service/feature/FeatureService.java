package com.featureswitch.featureswitch.service.feature;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;

public interface FeatureService {
    FeatureEntity getFeatureByName(String featureName) throws DataNotFoundException;
}
