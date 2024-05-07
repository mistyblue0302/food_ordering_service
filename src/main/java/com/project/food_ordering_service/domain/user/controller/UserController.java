package com.project.food_ordering_service.domain.user.controller;

import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.dto.UserSaveResponse;
import com.project.food_ordering_service.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
