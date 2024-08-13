package com.project.food_ordering_service.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "유효하지 않은 토큰입니다.")
public class UnacceptedAuthorizationException extends RuntimeException {

    public UnacceptedAuthorizationException() {
        super();
    }
}