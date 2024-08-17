package com.project.food_ordering_service.domain.auth.service;

import com.project.food_ordering_service.domain.auth.dto.LoginRequest;
import com.project.food_ordering_service.domain.auth.dto.LoginResponse;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.exception.CustomException;
import com.project.food_ordering_service.global.exception.ErrorInformation;
import com.project.food_ordering_service.global.utils.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.project.food_ordering_service.domain.utils.TestUtil.ROLE_CLIENT;
import static com.project.food_ordering_service.domain.utils.TestUtil.USER_ID;
import static com.project.food_ordering_service.domain.utils.TestUtil.savedUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    User user;

    LoginRequest loginRequest = new LoginRequest("emailMock@gmail.com", "123444@a");

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_success() {
        // given, when
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(CharSequence.class), any(String.class))).willReturn(true);
        given(user.getPassword()).willReturn(savedUser.getPassword());
        given(user.getId()).willReturn(USER_ID);
        given(user.getRole()).willReturn(ROLE_CLIENT);

        LoginResponse loginResponse = authService.login(loginRequest);
        assertThat(loginResponse).isNotNull();

        // then
        then(jwtUtil).should(times(1)).createAccessToken(USER_ID, ROLE_CLIENT);
        then(jwtUtil).should(times(1)).createRefreshToken(USER_ID, ROLE_CLIENT);
    }

    @Test
    @DisplayName("로그인 실패 테스트 : 이메일로 유저 조회 시 유저가 존재하지 않을 때")
    void login_failed_user_no_exists() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        // when, then
        CustomException exception = assertThrows(CustomException.class, () ->
                authService.login(loginRequest)
        );

        assertEquals(ErrorInformation.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 테스트 : 잘못된 비밀번호로 로그인 할 때")
    void login_failed_wrong_password() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(CharSequence.class), any(String.class))).willReturn(
                false);
        given(user.getPassword()).willReturn(savedUser.getPassword());

        // when, then
        CustomException exception = assertThrows(CustomException.class, () ->
                authService.login(loginRequest)
        );

        assertEquals(ErrorInformation.REQUEST_VALIDATION_FAIL.getMessage(), exception.getMessage());
    }
}