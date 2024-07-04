package com.project.food_ordering_service.global.utils.jwt;

import com.project.food_ordering_service.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * JWT 처리 유틸리티 클래스로 JWT 토큰을 생성하고 파싱한다. 대칭키 암호화 방식을 사용하여 비밀 키를 모르는 서버는 JWT 토큰을 생성할 수 없다.
 *
 * @key : JWT 토큰을 서명하고 검증하는 데 사용. 서버가 알고있는 비밀 키로 생성된다.
 * @accessTokenExpTime : 액세스 토큰 만료시간(초)
 * @refreshTokenExpTime : 리프레시 토큰 만료시간(초)
 */

/**
 * @Component : 런타임 시 스프링 빈으로 등록해준다.
 * @Value : application.yml 파일에서 설정 값을 주입받는다.
 */

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtil(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access_expiration_time}") long accessTokenExpirationTime,
        @Value("${jwt.refresh_expiration_time}") long refreshTokenExpirationTime
    ) {
        byte[] decodeKey = Decoders.BASE64.decode(secretKey); // 비밀 키 값을 디코딩
        this.key = Keys.hmacShaKeyFor(decodeKey); // SHA 키 생성
        this.accessTokenExpTime = accessTokenExpirationTime;
        this.refreshTokenExpTime = refreshTokenExpirationTime;
    }

    // 사용자 id로 액세스 토큰을 생성
    public String createAccessToken(Long id, Role role) {
        return createToken(id, role, JwtProperties.ACCESS_TOKEN_NAME, accessTokenExpTime);
    }

    // 사용자 id로 리프레시 토큰을 생성
    public String createRefreshToken(Long id, Role role) {
        return createToken(id, role, JwtProperties.REFRESH_TOKEN_NAME, refreshTokenExpTime);
    }

    // 사용자 id, 토큰 타입, 만료 시간을 사용해 JWT 토큰을 생성
    private String createToken(Long id, Role role, String tokenType, long expirationTime) {
        Claims claims = Jwts.claims(); // Jwt 클레임 생성
        claims.put(JwtProperties.USER_ID, id.toString()); // 클레임에 사용자 id값 추가
        claims.put(JwtProperties.USER_ROLE, role.name());

        ZonedDateTime publishedTime = ZonedDateTime.now(); // 현재 시간

        return Jwts.builder()
                .setHeaderParam(JwtProperties.TOKEN_TYPE, tokenType)
                .setClaims(claims)
                .setIssuedAt(Date.from(publishedTime.toInstant()))
                .setExpiration(Date.from(publishedTime.plusSeconds(expirationTime).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256) // SHA 키로 서명
                .compact(); // JWT 토큰 문자열로 생성
    }

    // 토큰을 파싱하여 클레임을 추출
    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
    }
}