package com.project.food_ordering_service.domain.user.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    public String email;

    public String password;
}
