package com.project.food_ordering_service.domain.user.dto;

import com.project.food_ordering_service.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Builder
public class UserSaveRequest {

    @NotNull
    private String loginId;

    @NotNull
    private String userName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String email;

    public User toEntity() {
        return User.builder()
            .loginId(this.loginId)
            .userName(this.userName)
            .phoneNumber(this.phoneNumber)
            .email(this.email)
            .build();
    }
}
