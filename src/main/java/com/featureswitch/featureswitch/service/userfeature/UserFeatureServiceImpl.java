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
    public UserFeatureEntity updatePermissionByUserEmailAndFeatureName(String userEmail, String featureName, boolean enable) throws DataNotFoundException {

        UserEntity user;
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (DataNotFoundException ex) {
            log.info("updatePermissionByUserEmailAndFeatureName(): User not found");
            throw ex;
        }

        FeatureEntity feature;
        try {
            feature = featureService.getFeatureByName(featureName);
        } catch (DataNotFoundException ex) {
            log.info("updatePermissionByUserEmailAndFeatureName(): Feature not found");
            throw ex;
        }

        UserFeatureEntity userFeature = userFeatureRepository.findByUserAndFeature(user, feature);

        if (userFeature != null && !enable) {
            // If there is a record in the table and we want to disable it, delete it from the table
            userFeatureRepository.delete(userFeature);
            log.info("updatePermissionByUserEmailAndFeatureName(): User state changed from enabled to disabled");
            return userFeature;
        } else if (userFeature == null && enable) {
            // If there is not a record in the
            // table and we want to enable a feature, add it to the table
            UserFeatureEntity newUserFeature = new UserFeatureEntity();
            newUserFeature.setUser(user);
            newUserFeature.setFeature(feature);
            log.info("updatePermissionByUserEmailAndFeatureName(): User state changed from disabled to enabled");
            return userFeatureRepository.save(newUserFeature);
        }

        return null;
    }

    @Override
    public boolean userHasPermission(String userEmail, String featureName) throws DataNotFoundException{

        UserEntity user;
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (DataNotFoundException ex) {
            log.info("userHasPermission(): User not found");
            throw ex;
        }

        log.debug(user.toString());

        FeatureEntity feature;
        try {
            feature = featureService.getFeatureByName(featureName);
        } catch (DataNotFoundException ex) {
            log.info("userHasPermission(): Feature not found");
            throw ex;
        }

        log.debug(feature.toString());

        UserFeatureEntity userFeature = userFeatureRepository.findByUserAndFeature(user, feature);
        if (userFeature != null) {
            log.info("userHasPermission(): User is enabled");
            return true;
        }

        log.info("userHasPermission(): User is not enabled");
        return false;
    }
}
