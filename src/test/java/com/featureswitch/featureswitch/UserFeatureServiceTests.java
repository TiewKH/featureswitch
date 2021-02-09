package com.featureswitch.featureswitch;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import com.featureswitch.featureswitch.exceptions.AddFailedException;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.repository.UserFeatureRepository;
import com.featureswitch.featureswitch.service.feature.FeatureService;
import com.featureswitch.featureswitch.service.user.UserService;
import com.featureswitch.featureswitch.service.userfeature.UserFeatureService;
import com.featureswitch.featureswitch.service.userfeature.UserFeatureServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserFeatureServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private FeatureService featureService;

    @Mock
    private UserFeatureRepository userFeatureRepository;

    private UserFeatureService userFeatureService;

    private final UserEntity userEntity = new UserEntity(1L, "test@test.com", null);
    private final FeatureEntity featureEntity = new FeatureEntity(1L, "Test Feature", null);

    @Before
    public void setUp() throws Exception {
        userFeatureService = new UserFeatureServiceImpl(featureService, userService, userFeatureRepository);
    }

    @Test
    public void testGetUserPermissionSuccessfulNotEnabled() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenReturn(featureEntity);
        when(userFeatureRepository.findByUserAndFeature(userEntity, featureEntity)).thenReturn(null);

        boolean hasPermission = userFeatureService.userHasPermission(userEntity.getEmail(), featureEntity.getFeatureName());
        assertThat(hasPermission).isNotNull();
        assertThat(hasPermission).isFalse();
    }

    @Test
    public void testGetUserPermissionSuccessfulEnabled() throws Exception {
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
        when(userService.getUserByEmail(userEntity.getEmail())).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> {
            userFeatureService.userHasPermission(userEntity.getEmail(), featureEntity.getFeatureName());
        });
    }

    @Test
    public void testGetUserPermissionFeatureDoesNotExist() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> {
            userFeatureService.userHasPermission(userEntity.getEmail(), featureEntity.getFeatureName());
        });
    }

    @Test
    public void testAddUserPermissionSuccessfulEnable() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenReturn(featureEntity);
        when(userFeatureRepository.findByUserAndFeature(userEntity, featureEntity)).thenReturn(null);

        UserFeatureEntity userFeatureEntity = new UserFeatureEntity();
        userFeatureEntity.setUser(userEntity);
        userFeatureEntity.setFeature(featureEntity);

        UserFeatureEntity addedUserFeatureEntity = new UserFeatureEntity(1L, userEntity, featureEntity);

        when(userFeatureRepository.save(userFeatureEntity)).thenReturn(addedUserFeatureEntity);

        UserFeatureEntity newUserFeatureEntity = userFeatureService.addPermissionByUserEmailAndFeatureName(userEntity.getEmail(), featureEntity.getFeatureName(), true);
        assertThat(newUserFeatureEntity).isNotNull();
        assertThat(newUserFeatureEntity).isEqualTo(addedUserFeatureEntity);
    }

    @Test
    public void testAddUserPermissionSuccessfulDisable() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenReturn(featureEntity);

        UserFeatureEntity userFeatureEntity = new UserFeatureEntity(1L, userEntity, featureEntity);
        when(userFeatureRepository.findByUserAndFeature(userEntity, featureEntity)).thenReturn(userFeatureEntity);

        UserFeatureEntity newUserFeatureEntity = userFeatureService.addPermissionByUserEmailAndFeatureName(userEntity.getEmail(), featureEntity.getFeatureName(), false);
        assertThat(newUserFeatureEntity).isNotNull();
        assertThat(newUserFeatureEntity).isEqualTo(userFeatureEntity);
        verify(userFeatureRepository, times(1)).delete(userFeatureEntity);
    }

    @Test
    public void testAddUserPermissionAlreadyEnabled() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenReturn(featureEntity);

        UserFeatureEntity userFeatureEntity = new UserFeatureEntity(1L, userEntity, featureEntity);
        when(userFeatureRepository.findByUserAndFeature(userEntity, featureEntity)).thenReturn(userFeatureEntity);

        assertThrows(AddFailedException.class, () -> {
            userFeatureService.addPermissionByUserEmailAndFeatureName(userEntity.getEmail(), featureEntity.getFeatureName(), true);
        });
    }

    @Test
    public void testAddUserPermissionAlreadyDisabled() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenReturn(featureEntity);
        when(userFeatureRepository.findByUserAndFeature(userEntity, featureEntity)).thenReturn(null);

        assertThrows(AddFailedException.class, () -> {
            userFeatureService.addPermissionByUserEmailAndFeatureName(userEntity.getEmail(), featureEntity.getFeatureName(), false);
        });
    }

    @Test
    public void testAddUserPermissionUserDoesNotExist() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenThrow(DataNotFoundException.class);

        assertThrows(AddFailedException.class, () -> {
            userFeatureService.addPermissionByUserEmailAndFeatureName(userEntity.getEmail(), featureEntity.getFeatureName(), true);
        });
    }

    @Test
    public void testAddUserPermissionFeatureDoesNotExist() throws Exception {
        when(userService.getUserByEmail(userEntity.getEmail())).thenReturn(userEntity);
        when(featureService.getFeatureByName(featureEntity.getFeatureName())).thenThrow(DataNotFoundException.class);

        assertThrows(AddFailedException.class, () -> {
            userFeatureService.addPermissionByUserEmailAndFeatureName(userEntity.getEmail(), featureEntity.getFeatureName(), true);
        });
    }
}
