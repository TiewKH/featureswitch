package com.featureswitch.featureswitch;

import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.repository.UserRepository;
import com.featureswitch.featureswitch.service.user.UserService;
import com.featureswitch.featureswitch.service.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testGetUserByEmailSuccessfully() throws Exception {
        UserEntity userEntity = new UserEntity(1L, "test@test.com", null);
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(userEntity);

        UserEntity findUserEntity = userService.getUserByEmail(userEntity.getEmail());
        assertThat(findUserEntity).isNotNull();
        assertThat(findUserEntity).isEqualTo(userEntity);
    }

    @Test
    public void testGetUserByEmailWhichDoesNotExist() throws Exception {
        UserEntity userEntity = new UserEntity(1L, "test@test.com", null);
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(null);
        assertThrows(DataNotFoundException.class, () -> {
            userService.getUserByEmail(userEntity.getEmail());
        });
    }
}
