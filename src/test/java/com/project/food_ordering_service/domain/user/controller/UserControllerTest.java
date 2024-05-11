package com.project.food_ordering_service.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @WebMvcTest : 스프링 애플리케이션 전체를 로드할 필요없이 특정 컨트롤러 레이어에 관련된 빈들만 로드하므로 테스트 속도가 빠르다.
 * @MockBean : mock 객체를 생성하고 주입하는데 사용된다. 특정 컴포넌트가 다른 컴포넌트에 의존하고 있을 때 외부 의존성을 목 객체로 대체하는데 사용된다.
 */

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void addUserTest() throws Exception {
        //given
        UserSaveRequest request = createUser();

        User savedUser = User.builder()
            .id(1L)
            .loginId(request.getLoginId())
            .userName(request.getUserName())
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .build();

        Mockito.when(userService.addUser(any(UserSaveRequest.class))).thenReturn(savedUser);

        //when
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUser())))
            //then
            .andExpect(status().isCreated());
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