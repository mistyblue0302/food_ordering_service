package com.project.food_ordering_service.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.food_ordering_service.domain.user.dto.UserPatchRequest;
import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.service.UserService;
import com.project.food_ordering_service.domain.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @ExtendWith : 단위 테스트에 공통적으로 사용할 확장 기능을 선언
 */

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void addUserTest() throws Exception {
        // given
        UserSaveRequest request = createUser();

        User savedUser = User.builder()
                .id(1L)
                .loginId("testId")
                .userName("testUserName")
                .password("testPassword")
                .phoneNumber("010-1234-5678")
                .email("test@gmail.com")
                .role(TestUtil.ROLE_CLIENT)
                .build();

        when(userService.addUser(any())).thenReturn(savedUser);

        // when
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 : 필수 필드가 누락")
    void addUserFailedTest() throws Exception {
        // given
        UserSaveRequest userSaveRequest = UserSaveRequest.builder()
                .loginId("testLoginIdFail")
                .password("testPassword")
                .build();

        // when
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSaveRequest))
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원정보 조회 테스트")
    void getUserTest() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .loginId("testLoginId")
                .userName("testName")
                .password("testPassword")
                .phoneNumber("010-1234-5678")
                .email("test@gmail.com")
                .role(TestUtil.ROLE_CLIENT)
                .build();

        when(userService.findUserById(anyLong())).thenReturn(user);

        // when
        mockMvc.perform(get("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원정보 수정 테스트")
    void modifyUserTest() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .loginId("testLoginId")
                .userName("testName2")
                .password("testPassword")
                .phoneNumber("010-1234-5679")
                .email("test@gmail.com")
                .role(TestUtil.ROLE_CLIENT)
                .build();

        UserPatchRequest userPatchRequest = UserPatchRequest.builder()
                .loginId("testLoginId")
                .userName("testName2")
                .phoneNumber("010-1234-5679")
                .modifiedBy("modifier")
                .build();

        when(userService.modifyUser(anyLong(), any(UserPatchRequest.class))).thenReturn(user);

        // when
        mockMvc.perform(patch("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchRequest))
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
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