package com.imguru.api.imageapi.service.util;

import com.imguru.api.imageapi.service.model.User;
import com.imguru.api.imageapi.service.model.UserEntity;

import java.util.List;

public class UserUtils {

    public static User wrapUserEntityToUser(UserEntity userEntity) {
        List<String> imageIds = userEntity.getImageIds();
        User user = User.builder().name(userEntity.getUsername()).password(userEntity.getPassword()).imageIds(imageIds).build();
        return user;
    }
}
