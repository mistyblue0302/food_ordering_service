package com.project.food_ordering_service.domain.auth.service;

import com.project.food_ordering_service.domain.auth.entity.RefreshToken;
import com.project.food_ordering_service.domain.auth.repository.AuthRepository;
import com.project.food_ordering_service.domain.auth.dto.LoginRequest;
import com.project.food_ordering_service.domain.auth.dto.LoginResponse;
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

        User user = userRepository.findByEmail(
                loginRequest.getEmail())
            .orElseThrow(() -> new UserNotFoundException());

        if (!passwordEncoder.matches(loginRequest.password, user.getPassword())) {
            throw new WrongPasswordException();
        }

        return createLoginResponse(user.getId());
    }

    @Transactional
    public void logout(JwtAuthentication jwtAuthentication) {
        RefreshToken logoutRefreshToken = RefreshToken.builder()
            .token(jwtAuthentication.getToken())
            .expiredDate(jwtAuthentication.getExpirationTime())
            .build();

        authRepository.save(logoutRefreshToken);
    }

    private LoginResponse createLoginResponse(Long userId) {
        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
