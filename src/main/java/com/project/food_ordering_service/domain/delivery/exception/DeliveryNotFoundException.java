package com.project.food_ordering_service.domain.delivery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "배달 정보를 찾을 수 없습니다.")
public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException() {
        super();
    }
}
