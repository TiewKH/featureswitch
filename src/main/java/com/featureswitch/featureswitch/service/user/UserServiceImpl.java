package com.featureswitch.featureswitch.service.user;

import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserEntity getUserByEmail(String email) throws DataNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new DataNotFoundException("No user found with the username: " + email);
        }

        return userEntity;
    }
}
