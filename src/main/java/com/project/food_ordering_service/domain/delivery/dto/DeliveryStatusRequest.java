package com.project.food_ordering_service.domain.delivery.dto;

import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusRequest {

    private OrderStatus status;
}
