package com.project.food_ordering_service.domain.delivery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DeliverArgumentException extends IllegalArgumentException {

    public DeliverArgumentException(String s) {
        super(s);
    }

}
