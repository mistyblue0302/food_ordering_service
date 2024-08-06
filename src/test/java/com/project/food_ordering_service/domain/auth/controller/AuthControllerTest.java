package com.project.food_ordering_service.domain.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.food_ordering_service.domain.auth.dto.LoginRequest;
import com.project.food_ordering_service.domain.auth.dto.LoginResponse;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.domain.utils.TestUtil;
import com.project.food_ordering_service.global.utils.jwt.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @SpringBootTest : SpringBoot 통합테스트에 사용되는 애노테이션으로 @SpringBootApplication을 찾아가 하위의 모든 빈을 스캔한다.
 * @AutoConfigureMockMvc
 * @ActiveProfiles
 */

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest extends TestUtil {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtUtil jwtUtil;

    String ACCESS_TOKEN;
    String REFRESH_TOKEN;

    @BeforeEach
    void setUp() {
        userRepository.save(savedUser);

        ACCESS_TOKEN = jwtUtil.createAccessToken(savedUser.getId(), savedUser.getRole());
        REFRESH_TOKEN = jwtUtil.createRefreshToken(savedUser.getId(), savedUser.getRole());
    }

    LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);

    @Test
    @DisplayName("로그인 테스트 : 엑세스 토큰과 리프레시 토큰을 반환")
    void login_success() throws Exception {
        // given, when
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }
    @Test
    @DisplayName("로그아웃 테스트")
    void logout_success() throws Exception {
        //given
        Jws<Claims> claimsJws = jwtUtil.parseToken(REFRESH_TOKEN);
        JwtHolder jwtHolder = new JwtHolder(claimsJws, REFRESH_TOKEN);
        JwtAuthentication jwtAuthentication = new JwtAuthentication(jwtHolder);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
                jwtAuthentication,
                List.of(new SimpleGrantedAuthority(Role.CLIENT.toString()))
        );

        //when, then
        mockMvc.perform(post("/auth/logout")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(jwtAuthenticationToken)))
                .andExpect(status().isOk());
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