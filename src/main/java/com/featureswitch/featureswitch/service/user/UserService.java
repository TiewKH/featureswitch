package com.featureswitch.featureswitch.service.user;

import com.featureswitch.featureswitch.entity.UserEntity;

public interface UserService {
    UserEntity getUserByEmail(String email);
}
