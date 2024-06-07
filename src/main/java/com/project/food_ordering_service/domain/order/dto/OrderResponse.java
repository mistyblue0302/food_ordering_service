package com.project.food_ordering_service.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {

    private Long id;

    private Long userId;

    private Long restaurantId;

    private String status;
}
