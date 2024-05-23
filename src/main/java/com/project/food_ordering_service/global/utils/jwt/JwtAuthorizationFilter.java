package com.project.food_ordering_service.global.utils.jwt;

import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.global.utils.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 토큰에 대한 검증을 수행 토큰의 유효성 검사 및 사용자 인증 정보를 설정
 */

/**
 * @Slf4j :  로깅을 위한 추상화 라이브러리
 * @Component : 런타임 시 스프링 빈으로 등록해준다.
 * @RequiredArgsConstructor : final 필드의 생성자를 생성
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends
    OncePerRequestFilter { // OnecePerRequestFilter : 한 요청당 한 번 실행 보장

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(JwtProperties.HEADER);

        /**
         * Jwt를 사용한 인증은 Authorization 헤더에 Bearer라는 접두사를 붙여 토큰을 포함
         * 예시 -> Authorization: Bearer eyJhbGciOiJIUzI1N...
         */
        if (authorizationHeader != null && authorizationHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            String token = authorizationHeader.substring(JwtProperties.TOKEN_PREFIX_LENGTH);
            // 토큰을 추출하고 proceedAuthentication() 메소드를 호출해 인증 진행
            proceedAuthentication(request, token);
        }
        filterChain.doFilter(request, response); // 다음 필터로 요청과 응답 전달
    }

    // 토큰을 검증
    private void proceedAuthentication(HttpServletRequest request, String token) {
        try {
            JwtHolder jwtHolder = new JwtHolder(jwtUtil.parseToken(token), token);
            // 토큰 파싱 후 요청이 리프레시 토큰 기반인지, 엑세스 토큰 기반인지 확인
            if (isRefreshTokenBasedRequest(request, jwtHolder) || isAccessTokenBasedRequest(request,
                jwtHolder)) {
                setAuthenticationFromJwt(jwtHolder); // 인증 정보 설정
            }
        } catch (MalformedJwtException | SignatureException e) { // 유효하지 않은 토큰 예외 처리
            logInvalidTokenException(e, token);
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) { // 만료된 토큰 예외 처리
            logInvalidTokenException(e, token);
            throw new InvalidTokenException();
        } catch (UnsupportedJwtException e) { // 지원되지 않는 토큰 예외 처리
            logInvalidTokenException(e, token);
            throw new InvalidTokenException();
        } catch (IllegalArgumentException e) { // 잘못된 인자 예외 처리
            logInvalidTokenException(e, token);
            throw new InvalidTokenException();
        }
    }

    // 토큰 파싱 중 예외가 발생했을 때 로그를 남기는 메소드
    private void logInvalidTokenException(Exception e, String token) {
        log.info("exception : {} " + "token : {}", e.getClass().getSimpleName(), token);
    }

    private boolean isRefreshTokenBasedRequest(HttpServletRequest request, JwtHolder jwtHolder) {
        return isRequiredRefreshToken(request) && jwtHolder.isRefreshToken();
    }

    private boolean isAccessTokenBasedRequest(HttpServletRequest request, JwtHolder jwtHolder) {
        return !isRequiredRefreshToken(request) && jwtHolder.isAccessToken();
    }

    // 리프레시 토큰이 필요한 요청인지 확인(로그아웃 또는 토큰 재발급을 위한 요청)
    private boolean isRequiredRefreshToken(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/logout") || request.getRequestURI()
            .endsWith("/reissue");
    }

    // JWT 토큰에서 인증정보 설정
    private void setAuthenticationFromJwt(JwtHolder jwtHolder) {
        // JwtHolder 객체를 통해 토큰에 필요한 정보를 가져온다. (토큰 문자열, 사용자 식별, 만료시간)
        JwtAuthentication jwtAuthentication = new JwtAuthentication(jwtHolder);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
            jwtAuthentication, List.of(
            new SimpleGrantedAuthority(Role.CLIENT.toString())));

        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
    }
}
