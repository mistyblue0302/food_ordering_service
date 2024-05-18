package com.project.food_ordering_service.global.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * JWT 토큰에서 클레임과 관련된 정보를 추출하는 클래스
 */

/**
 * @Slf4j :  로깅을 위한 추상화 라이브러리
 * @RequiredArgsConstructor : final 필드의 생성자를 생성
 */

@Slf4j
@RequiredArgsConstructor
public class JwtHolder {

    private final String errorMessage = "jwt claim 필드 null ";
    private final Jws<Claims> claims;
    private final String token;

    public Long getUserId() {
        return Long.parseLong(String.valueOf(claims.getBody().get("userId")));
    }

    public String getToken() {
        return token;
    }

    public boolean isAccessToken() {
        return claims.getHeader().get("token").toString().equals("access");
    }

    public boolean isRefreshToken() {
        return claims.getHeader().get("token").toString().equals("refresh");
    }

    public Date getExpirationTime() {
        return claims.getBody().getExpiration();
    }
}
