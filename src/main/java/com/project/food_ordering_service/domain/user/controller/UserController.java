package com.project.food_ordering_service.domain.user.controller;

import com.project.food_ordering_service.domain.user.dto.*;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @RestController : 해당 클래스가 Restful 웹 서비스의 컨트롤러임을 나타내는 애노테이션으로 HTTP 요청과 응답을 처리하는 컨트롤러로 사용
 * @RequiredArgsConstructor : final 필드의 생성자를 생성해주는 애노테이션
 * @RequestMapping : 요청 URL과 이를 처리하는 메소드를 매핑하기 위한 애노테이션으로 컨트롤러 메소드가 처리할 URL 패턴을 지정
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserSaveResponse> addUser(
            @RequestBody @Validated UserSaveRequest userSaveRequest) {
        userService.addUser(userSaveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetResponse> getUser(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        UserGetResponse userGetResponse = UserGetResponse.from(user);
        return ResponseEntity.ok(userGetResponse);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserPatchResponse> modifyUser(@PathVariable Long userId, @RequestBody UserPatchRequest userPatchRequest) {
        User user = userService.modifyUser(userId, userPatchRequest);
        return new ResponseEntity<>(UserPatchResponse.from(user), HttpStatus.OK);
    }
}
