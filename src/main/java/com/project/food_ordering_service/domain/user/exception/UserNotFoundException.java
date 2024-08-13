package com.project.food_ordering_service.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "사용자 정보를 찾을 수 없습니다.")
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super();
    }
}
