package com.project.food_ordering_service.domain.user.dto;

import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Getter : Lombok 라이브러리에서 제공하는 애노테이션으로 getter 메소드를 자동으로 생성
 * @Builder : 빌더 패턴을 사용할 수 있게 도와주는 애노테이션
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequest {

    @NotBlank(message = "{loginId.notBlank}")
    private String loginId;

    @NotBlank(message = "{userName.notBlank}")
    private String userName;

    @NotBlank(message = "{password.notBlank}")
    private String password;

    @NotBlank(message = "{phoneNumber.notBlank}")
    private String phoneNumber;

    @NotBlank(message = "{email.notBlank}")
    private String email;

    @NotNull(message = "{role.notNull}")
    private Role role;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .userName(this.userName)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .role(this.role)
                .build();
    }
}
