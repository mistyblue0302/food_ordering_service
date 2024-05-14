package com.project.food_ordering_service.domain.user.dto;

import com.project.food_ordering_service.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String loginId;
    private String name;

    public static LoginResponse from(User user) {
        return LoginResponse.builder()
            .loginId(user.getLoginId())
            .name(user.getUserName())
            .build();
    }
}
