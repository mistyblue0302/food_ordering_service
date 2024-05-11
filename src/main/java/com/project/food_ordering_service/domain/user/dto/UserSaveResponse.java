package com.project.food_ordering_service.domain.user.dto;

import com.project.food_ordering_service.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * @Getter : Lombok 라이브러리에서 제공하는 애노테이션으로 getter 메소드를 자동으로 생성
 * @Builder : 빌더 패턴을 사용할 수 있게 도와주는 애노테이션
 */

@Getter
@Builder
public class UserSaveResponse {

    private Long id;
    private String loginId;
    private String userName;
    private String phoneNumber;
    private String email;
    private LocalDateTime createdAt;

    public static UserSaveResponse fromUser(User user) {
        return UserSaveResponse.builder()
            .id(user.getId())
            .loginId(user.getLoginId())
            .userName(user.getUserName())
            .phoneNumber(user.getPhoneNumber())
            .email(user.getEmail())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
