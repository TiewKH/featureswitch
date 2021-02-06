package com.featureswitch.featureswitch.service.userfeature;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.repository.UserFeatureRepository;
import com.featureswitch.featureswitch.service.feature.FeatureService;
import com.featureswitch.featureswitch.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFeatureServiceImpl implements UserFeatureService{
    private final FeatureService featureService;
    private final UserService userService;
    private final UserFeatureRepository userFeatureRepository;

    @Override
    public UserFeatureEntity updateByUserEmailAndFeatureName(String userEmail, String featureName, boolean enable) throws DataNotFoundException {

        UserEntity user;
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (DataNotFoundException ex) {
            log.info("updateByUserEmailAndFeatureName(): User not found");
            throw ex;
        }

        FeatureEntity feature;
        try {
            feature = featureService.getFeatureByName(featureName);
        } catch (DataNotFoundException ex) {
            log.info("updateByUserEmailAndFeatureName(): Feature not found");
            throw ex;
        }

        UserFeatureEntity userFeature = userFeatureRepository.findByUserIdAndFeatureId(user.getUserId(), feature.getFeatureId());

        if (userFeature != null && !enable) {
            // If there is a record in the table and we want to disable it, delete it from the table
            userFeatureRepository.delete(userFeature);
            log.info("User is disabled");
            return userFeature;
        } else if (userFeature == null && enable) {
            // If there is not a record in the table and we want to enable a feature, add it to the table
            UserFeatureEntity newUserFeature = new UserFeatureEntity();
            newUserFeature.setUserId(user.getUserId());
            newUserFeature.setFeatureId(feature.getFeatureId());
            log.info("User is enabled");
            return userFeatureRepository.save(newUserFeature);
        }

        return null;
    }

    @Override
    public boolean userIsEnabled(String userEmail, String featureName) throws DataNotFoundException{

        UserEntity user;
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (DataNotFoundException ex) {
            log.info("userIsEnabled(): User not found");
            throw ex;
        }

        FeatureEntity feature;
        try {
            feature = featureService.getFeatureByName(featureName);
        } catch (DataNotFoundException ex) {
            log.info("userIsEnabled(): Feature not found");
            throw ex;
        }

        UserFeatureEntity userFeature = userFeatureRepository.findByUserIdAndFeatureId(user.getUserId(), feature.getFeatureId());
        if (userFeature != null) {
            log.info("User is enabled");
            return true;
        }

        log.info("User is not enabled");
        return false;
    }
}
