package com.featureswitch.featureswitch.service.userfeature;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.entity.UserFeatureEntity;
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
    public UserFeatureEntity updateByUserEmailAndFeatureName(String userEmail, String featureName, boolean enable) {
        UserEntity user = userService.getUserByEmail(userEmail);
        if (user == null) {
            log.info("User does not exist");
            return null;
        }

        FeatureEntity feature = featureService.getFeatureByName(featureName);
        if (feature == null) {
            log.info("Feature does not exist");
            return null;
        }

        UserFeatureEntity userFeature = userFeatureRepository.findByUserIdAndFeatureId(user.getUserId(), feature.getFeatureId());
        if (userFeature != null) {
            if (!enable) {
                userFeatureRepository.delete(userFeature);
                log.info("User is disabled");
                return userFeature;
            }
        }

        if (enable) {
            UserFeatureEntity newUserFeature = new UserFeatureEntity();
            newUserFeature.setUserId(user.getUserId());
            newUserFeature.setFeatureId(feature.getFeatureId());
            log.info("User is enabled");
            return userFeatureRepository.save(newUserFeature);
        }

        return null;
    }

    @Override
    public boolean userIsEnabled(String userEmail, String featureName) {
        UserEntity user = userService.getUserByEmail(userEmail);
        if ( user == null) {
            log.info("User does not exist");
            return false;
        }

        FeatureEntity feature = featureService.getFeatureByName(featureName);
        if (feature == null) {
            log.info("Feature does not exist");
            return false;
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
