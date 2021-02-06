package com.featureswitch.featureswitch.service.user;

import com.featureswitch.featureswitch.entity.UserEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;

public interface UserService {
    UserEntity getUserByEmail(String email) throws DataNotFoundException;
}
