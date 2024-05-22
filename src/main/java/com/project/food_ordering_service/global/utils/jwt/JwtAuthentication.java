package com.project.food_ordering_service.global.utils.jwt;

import java.util.Date;
import lombok.Getter;

/**
 * 해당 클래스는 JWT 토큰 인증 정보를 저장 token : JWT 토큰 값을 저장 id : 사용자 id를 저장 expirationTime : 토큰의 만료시간 저장
 */

@Getter
public class JwtAuthentication {

    private final String token;
    private final Long id;
    private final Date expirationTime;

    public JwtAuthentication(JwtHolder jwtHolder) {
        this.token = jwtHolder.getToken();
        this.id = jwtHolder.getUserId();
        this.expirationTime = jwtHolder.getExpirationTime();
    }
}