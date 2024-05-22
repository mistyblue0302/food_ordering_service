package com.project.food_ordering_service.domain.auth.dto;

import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;

public record UserInfo(Long id,
                       String email,
                       String userName,
                       Role role) {

    public static UserInfo from(User user) {
        return new UserInfo(
            user.getId()
            , user.getEmail()
            , user.getUserName()
            , user.getRole());
    }
}
