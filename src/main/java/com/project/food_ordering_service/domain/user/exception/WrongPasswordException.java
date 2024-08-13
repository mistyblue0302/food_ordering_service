package com.project.food_ordering_service.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "잘못된 비밀번호입니다.")
public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super();
    }
}

