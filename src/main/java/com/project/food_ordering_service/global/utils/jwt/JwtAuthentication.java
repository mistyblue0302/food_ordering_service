package com.project.food_ordering_service.global.utils.jwt;


import lombok.Getter;

import java.util.Date;

/**
 * 해당 클래스는 JWT 토큰 인증 정보를 저장
 * token : JWT 토큰 값을 저장
 * id : 사용자 id를 저장
 * expirationTime : 토큰의 만료시간 저장
 */

@Getter
public record JwtAuthentication(String token, Long id, Date expirationTime) {
}
