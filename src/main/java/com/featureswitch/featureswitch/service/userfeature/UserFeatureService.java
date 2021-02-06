package com.featureswitch.featureswitch.service.userfeature;

import com.featureswitch.featureswitch.entity.UserFeatureEntity;

public interface UserFeatureService {
    UserFeatureEntity updateByUserEmailAndFeatureName(String userEmail, String featureName, boolean enable);
    boolean userIsEnabled(String userEmail, String featureName);
}
