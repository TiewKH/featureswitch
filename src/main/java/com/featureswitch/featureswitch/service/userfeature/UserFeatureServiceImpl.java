package com.featureswitch.featureswitch.service.userfeature;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.exceptions.AddFailedException;
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
    public UserFeatureEntity addPermissionByUserEmailAndFeatureName(String userEmail, String featureName, boolean enable) throws AddFailedException {

        UserEntity user;
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (DataNotFoundException ex) {
            log.info("addPermissionByUserEmailAndFeatureName(): User not found");
            throw new AddFailedException(ex.getMessage());
        }

        FeatureEntity feature;
        try {
            feature = featureService.getFeatureByName(featureName);
        } catch (DataNotFoundException ex) {
            log.info("addPermissionByUserEmailAndFeatureName(): Feature not found");
            throw new AddFailedException(ex.getMessage());
        }

        UserFeatureEntity userFeatureEntity = userFeatureRepository.findByUserAndFeature(user, feature);

        if (enable) {
            // If there is no record and user wants to enable, add the record in the table
            if (userFeatureEntity == null) {
                UserFeatureEntity newUserFeatureEntity = new UserFeatureEntity();
                newUserFeatureEntity.setUser(user);
                newUserFeatureEntity.setFeature(feature);
                log.info("addPermissionByUserEmailAndFeatureName(): User state changed from disabled to enabled");
                return userFeatureRepository.save(newUserFeatureEntity);
            }
            // If there is a record and user wants to enable, do not need to modify
            log.info("addPermissionByUserEmailAndFeatureName(): User is already enabled");
            throw new AddFailedException("User is already enabled");
        }

        // If there is no record and user wants to disable, do not need to modify
        if (userFeatureEntity == null) {
            log.info("addPermissionByUserEmailAndFeatureName(): User is already disabled");
            throw new AddFailedException("User is already disabled");
        }

        // If there is a record and user wants to disable, delete it from the table
        userFeatureRepository.delete(userFeatureEntity);
        log.info("addPermissionByUserEmailAndFeatureName(): User state changed from enabled to disabled");
        return userFeatureEntity;
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
