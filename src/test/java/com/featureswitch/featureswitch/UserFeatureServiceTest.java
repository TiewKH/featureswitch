package com.featureswitch.featureswitch;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.repository.UserFeatureRepository;
import com.featureswitch.featureswitch.repository.UserRepository;
import com.featureswitch.featureswitch.service.feature.FeatureService;
import com.featureswitch.featureswitch.service.user.UserService;
import com.featureswitch.featureswitch.service.user.UserServiceImpl;
import com.featureswitch.featureswitch.service.userfeature.UserFeatureService;
import com.featureswitch.featureswitch.service.userfeature.UserFeatureServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserFeatureServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private FeatureService featureService;

    @Mock
    private UserFeatureRepository userFeatureRepository;

    private UserFeatureService userFeatureService;

    private UserEntity userEntity = new UserEntity(1L, "test@test.com", null);
    private FeatureEntity featureEntity = new FeatureEntity(1L, "Test Feature", null);

    @Before
    public void setUp() throws Exception {
        userFeatureService = new UserFeatureServiceImpl(featureService, userService, userFeatureRepository);
    }

    @Test
    public void testGetUserPermissionSuccessfullyNotEnabled() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenReturn(featureEntity);
        when(userFeatureRepository.findByUserAndFeature(userEntity, featureEntity)).thenReturn(null);

        boolean hasPermission = userFeatureService.userHasPermission(userEntity.getEmail(), featureEntity.getFeatureName());
        assertThat(hasPermission).isNotNull();
        assertThat(hasPermission).isFalse();
    }

    @Test
    public void testGetUserPermissionSuccessfullyEnabled() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenReturn(featureEntity);

        UserFeatureEntity userFeatureEntity = new UserFeatureEntity(1L, userEntity, featureEntity);
        when(userFeatureRepository.findByUserAndFeature(userEntity, featureEntity)).thenReturn(userFeatureEntity);

        boolean hasPermission = userFeatureService.userHasPermission(userEntity.getEmail(), featureEntity.getFeatureName());
        assertThat(hasPermission).isNotNull();
        assertThat(hasPermission).isTrue();
    }

    @Test
    public void testGetUserPermissionUserDoesNotExist() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> {
            userFeatureService.userHasPermission(userEntity.getEmail(), featureEntity.getFeatureName());
        });
    }

    @Test
    public void testGetUserPermissionFeatureDoesNotExist() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);

        FeatureEntity featureEntity = new FeatureEntity(1L, "Test Feature", null);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> {
            userFeatureService.userHasPermission(userEntity.getEmail(), featureEntity.getFeatureName());
        });
    }
}
