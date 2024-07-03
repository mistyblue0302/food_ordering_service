package com.project.food_ordering_service.global.utils.jwt;

import com.project.food_ordering_service.domain.user.entity.Role;
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

    private final String errorMessage = "jwt claim 필드 null : ";
    private final Jws<Claims> claims;
    private final String token;

    public Long getUserId() {
        try {
            String userId = claims.getBody().get(JwtProperties.USER_ID).toString();
            return Long.parseLong(userId);
        } catch (NullPointerException e) {
            log.info(errorMessage + JwtProperties.USER_ID);
            throw new IllegalArgumentException();
        }
    }

    public String getToken() {
        return token;
    }

    public boolean isAccessToken() {
        return isRightToken(JwtProperties.ACCESS_TOKEN_NAME);
    }

    public boolean isRefreshToken() {
        return isRightToken(JwtProperties.REFRESH_TOKEN_NAME);
    }

    private boolean isRightToken(String tokenType) {
        try {
            return claims.getHeader().get(JwtProperties.TOKEN_TYPE).toString().equals(tokenType);
        } catch (NullPointerException e) {
            log.info(errorMessage + tokenType);
            throw new IllegalArgumentException();
        }
    }

    public Date getExpirationTime() {
        return claims.getBody().getExpiration();
    }

    public Role getRole() {
        try {
            String role = claims.getBody().get(JwtProperties.USER_ROLE).toString();
            return Role.valueOf(role);
        } catch (NullPointerException e) {
            log.info(errorMessage + JwtProperties.USER_ROLE);
            throw new IllegalArgumentException();
        }
    }
}
