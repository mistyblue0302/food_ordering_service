package com.project.food_ordering_service.domain.user.dto;

import com.project.food_ordering_service.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * @Getter : Lombok 라이브러리에서 제공하는 애노테이션으로 getter 메소드를 자동으로 생성
 * @Builder : 빌더 패턴을 사용할 수 있게 도와주는 애노테이션
 */

@Getter
@Setter
@Builder
public class UserSaveRequest {

    @NotNull
    private String loginId;

    @NotNull
    private String userName;

    @NotNull
    private String password;

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
