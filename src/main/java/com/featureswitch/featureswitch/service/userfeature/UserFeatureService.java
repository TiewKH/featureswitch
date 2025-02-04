package com.featureswitch.featureswitch.service.userfeature;

import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.exceptions.AddFailedException;

public interface UserFeatureService {
    UserFeatureEntity addPermissionByUserEmailAndFeatureName(String userEmail, String featureName, boolean enable) throws AddFailedException;

    // Putting this here because we are also checking if the feature doesn't exist
    // If we are only checking for user and not if the feature exists, we can put it in UserService instead
    boolean userHasPermission(String userEmail, String featureName) throws DataNotFoundException;
}
