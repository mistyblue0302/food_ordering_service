package com.project.food_ordering_service.global.utils.jwt;

import java.util.Date;

import com.project.food_ordering_service.domain.user.entity.Role;
import lombok.Getter;

/**
 * 해당 클래스는 JWT 토큰 인증 정보를 저장
 * token : JWT 토큰 값
 * id : 사용자 id
 * role : 사용자 역할
 * expirationTime : 토큰의 만료시간
 */

@Getter
public class JwtAuthentication {

    private final String token;
    private final Long id;
    private final Role role;
    private final Date expirationTime;

    public JwtAuthentication(JwtHolder jwtHolder) {
        this.token = jwtHolder.getToken();
        this.id = jwtHolder.getUserId();
        this.role = jwtHolder.getRole();
        this.expirationTime = jwtHolder.getExpirationTime();
    }
}