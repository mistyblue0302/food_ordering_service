package com.project.food_ordering_service.domain.order.dto;


import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStateRequest {

    private OrderStatus status;
}
