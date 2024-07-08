package com.project.food_ordering_service.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.exception.DuplicatedEmailException;
import com.project.food_ordering_service.domain.user.exception.DuplicatedLoginIdException;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.domain.utils.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ExtendWith(MockitoExtension.class) : JUnit5에서 Mockito를 사용하는 테스트 클래스에서 Mockito의 기능을 활용할 수 있도록 해주는
 * 애노테이션으로, 테스트 클래스에 포함된 Mockito 관련 애노테이션들(@Mock, @InjectMocks, @Spy, @Captor 등)이 동작하게 되어, mock 객체를
 * 생성하고 설정하거나 주입하는 등의 작업을 보다 편리하게 할 수 있다.
 */

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("중복 이메일 예외 발생 테스트")
    void DuplicatedEmailExceptionTest() throws Exception {
        // given
        given(userRepository.existsByEmail(anyString()))
                .willReturn(true);

        // when
        assertThatThrownBy(() -> userService.addUser(createUser()))
                // then
                .isInstanceOf(DuplicatedEmailException.class);
    }

    @Test
    @DisplayName("중복 로그인 아이디 예외 발생 테스트")
    void DuplicatedLoginIdExceptionTest() throws Exception {
        // given
        given(userRepository.existsByLoginId(anyString()))
                .willReturn(true);

        // when
        assertThatThrownBy(() -> userService.addUser(createUser()))
                // then
                .isInstanceOf(DuplicatedLoginIdException.class);
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void addUser() {
        // given
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByLoginId(anyString())).willReturn(false);

        UserSaveRequest userSaveRequest = createUser();

        // when
        User savedUser = User.builder()
                .id(1L)
                .loginId(userSaveRequest.getLoginId())
                .userName(userSaveRequest.getUserName())
                .password(userSaveRequest.getPassword())
                .phoneNumber(userSaveRequest.getPhoneNumber())
                .email(userSaveRequest.getEmail())
                .role(userSaveRequest.getRole())
                .build();

        userService.addUser(userSaveRequest);

        // then
        assertEquals(userSaveRequest.getLoginId(), savedUser.getLoginId());
        assertEquals(userSaveRequest.getUserName(), savedUser.getUserName());
        assertEquals(userSaveRequest.getEmail(), savedUser.getEmail());
        assertEquals(userSaveRequest.getPhoneNumber(), savedUser.getPhoneNumber());
        assertEquals(userSaveRequest.getRole(), savedUser.getRole());
    }

    private UserSaveRequest createUser() {
        return UserSaveRequest.builder()
                .loginId("testId")
                .userName("testUserName")
                .password("testPassword")
                .phoneNumber("010-1234-5678")
                .email("test@gmail.com")
                .role(TestUtil.ROLE_CLIENT)
                .build();
    }
}