package com.project.food_ordering_service.domain.auth.service;

import com.project.food_ordering_service.domain.auth.entity.RefreshToken;
import com.project.food_ordering_service.domain.auth.exception.UnacceptedAuthrizationException;
import com.project.food_ordering_service.domain.auth.repository.AuthRepository;
import com.project.food_ordering_service.domain.auth.dto.LoginRequest;
import com.project.food_ordering_service.domain.auth.dto.LoginResponse;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.exception.UserNotFoundException;
import com.project.food_ordering_service.domain.user.exception.WrongPasswordException;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import com.project.food_ordering_service.global.utils.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Service : 해당 클래스가 비즈니스 로직을 수행하는 서비스 클래스임을 나타내는 애노테이션
 * @RequiredArgsConstructor : final 필드의 생성자를 생성해주는 애노테이션
 */

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }

        return createLoginResponse(user.getId(), user.getRole());
    }

    @Transactional
    public void logout(JwtAuthentication jwtAuthentication) {
        RefreshToken logoutRefreshToken = RefreshToken.builder()
                .token(jwtAuthentication.getToken())
                .expiredDate(jwtAuthentication.getExpirationTime())
                .build();

        authRepository.save(logoutRefreshToken);
    }

    @Transactional
    public LoginResponse reissue(JwtAuthentication jwtAuthentication) {
        // 토큰을 추출하여 이미 로그아웃 처리된 토큰인지 확인
        if (authRepository.existsByToken(jwtAuthentication.getToken())) {
            throw new UnacceptedAuthrizationException();
        }

        // 로그아웃 처리되지 않은 토큰이라면 새로운 엑세스 토큰과 리프레시 토큰을 생성하여 반환
        return createLoginResponse(jwtAuthentication.getId(), jwtAuthentication.getRole());
    }

    private LoginResponse createLoginResponse(Long userId, Role role) {
        String accessToken = jwtUtil.createAccessToken(userId, role);
        String refreshToken = jwtUtil.createRefreshToken(userId, role);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
