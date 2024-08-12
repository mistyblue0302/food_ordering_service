package com.project.food_ordering_service.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "이미 존재하는 이메일입니다.")
public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException() {
        super();
    }
}
