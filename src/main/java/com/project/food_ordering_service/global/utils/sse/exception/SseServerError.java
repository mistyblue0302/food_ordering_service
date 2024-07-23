package com.project.food_ordering_service.global.utils.sse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class SseServerError extends RuntimeException {

    public SseServerError(String message, Throwable cause) {
        super(message, cause);
    }
}

