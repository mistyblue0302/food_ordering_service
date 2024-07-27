package com.project.food_ordering_service.domain.order.dto;


import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStateRequest {

    private OrderStatus status;
}
