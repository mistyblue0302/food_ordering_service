package com.project.food_ordering_service.domain.auth.controller;

import com.project.food_ordering_service.domain.auth.dto.LogoutResponse;
import com.project.food_ordering_service.domain.auth.service.AuthService;
import com.project.food_ordering_service.domain.auth.dto.LoginRequest;
import com.project.food_ordering_service.domain.auth.dto.LoginResponse;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController : 해당 클래스가 Restful 웹 서비스의 컨트롤러임을 나타내는 애노테이션으로 HTTP 요청과 응답을 처리하는 컨트롤러로 사용
 * @RequiredArgsConstructor : final 필드의 생성자를 생성해주는 애노테이션
 * @RequestMapping : 요청 URL과 이를 처리하는 메소드를 매핑하기 위한 애노테이션으로 컨트롤러 메소드가 처리할 URL 패턴을 지정
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Validated LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        authService.logout(jwtAuthentication);
        LogoutResponse response = new LogoutResponse("Logout successful");
        return ResponseEntity.ok(response);
    }
}
