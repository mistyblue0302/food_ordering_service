package com.project.food_ordering_service.domain.user.entity;

import lombok.Getter;

/**
 * 주문상태 WAITING :대기 CANCEL : 취소 ACTIVE : 조리중 DONE: 완료
 */
@Getter
public enum Status {

    WAITING,
    CANCEL,
    ACTIVE,
    DONE

}
