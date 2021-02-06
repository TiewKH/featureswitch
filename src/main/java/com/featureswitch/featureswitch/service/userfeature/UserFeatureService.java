package com.featureswitch.featureswitch.service.userfeature;

import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;

public interface UserFeatureService {
    UserFeatureEntity updateByUserEmailAndFeatureName(String userEmail, String featureName, boolean enable) throws DataNotFoundException;
    boolean userIsEnabled(String userEmail, String featureName) throws DataNotFoundException;
}
