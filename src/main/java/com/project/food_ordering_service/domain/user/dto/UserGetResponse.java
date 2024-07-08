package com.project.food_ordering_service.domain.user.dto;

import com.project.food_ordering_service.domain.user.entity.User;
import lombok.Builder;

@Builder
public class UserGetResponse {

    private User user;

    public static UserGetResponse from(User user) {
        return UserGetResponse.builder()
                .user(user)
                .build();
    }
}
