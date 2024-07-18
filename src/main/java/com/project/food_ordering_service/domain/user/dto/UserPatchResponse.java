package com.project.food_ordering_service.domain.user.dto;

import com.project.food_ordering_service.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserPatchResponse {

    private String loginId;
    private String userName;
    private String phoneNumber;
    private String email;
    private LocalDateTime modifiedAt;

    public static UserPatchResponse from(User user) {
        return UserPatchResponse.builder()
                .loginId(user.getLoginId())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}