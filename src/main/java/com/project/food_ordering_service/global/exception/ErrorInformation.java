package com.project.food_ordering_service.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorInformation {

    // 400
    REQUEST_VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "유효한 요청 값이 아닙니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    //403
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한 오류입니다."),

    // 404
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배달 정보입니다."),

    // 409
    DUPLICATE_MEMBER(HttpStatus.CONFLICT, "이미 사용 중인 계정입니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 관리자에게 문의 주세요.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorInformation(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
