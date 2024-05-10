package com.project.food_ordering_service.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.exception.DuplicatedEmailException;
import com.project.food_ordering_service.domain.user.exception.DuplicatedLoginIdException;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("중복 이메일 예외 발생 테스트")
    void DuplicatedEmailExceptionTest() throws Exception {
        //given
        given(userRepository.existsByEmail(anyString()))
            .willReturn(true);

        //when
        assertThatThrownBy(() -> userService.addUser(createUser()))
            //then
            .isInstanceOf(DuplicatedEmailException.class);
    }

    @Test
    @DisplayName("중복 로그인 아이디 예외 발생 테스트")
    void DuplicatedLoginIdExceptionTest() throws Exception {
        //given
        given(userRepository.existsByLoginId(anyString()))
            .willReturn(true);

        //when
        assertThatThrownBy(() -> userService.addUser(createUser()))
            //then
            .isInstanceOf(DuplicatedLoginIdException.class);
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void addUserTest() throws Exception {
        //given, when
        given(userRepository.existsByEmail(anyString()))
            .willReturn(false);
        given(userRepository.existsByLoginId(anyString()))
            .willReturn(false);

        userService.addUser(createUser());

        //then
        then(userRepository).should(times(1)).save(any(User.class));
    }

    private UserSaveRequest createUser() {
        return UserSaveRequest.builder()
            .loginId("testId")
            .userName("testUserName")
            .phoneNumber("010-1234-5678")
            .email("test@gmail.com")
            .build();
    }
}