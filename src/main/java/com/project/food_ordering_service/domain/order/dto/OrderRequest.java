package com.project.food_ordering_service.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequest {

    private Long id;

    private Long restaurantId;
}
