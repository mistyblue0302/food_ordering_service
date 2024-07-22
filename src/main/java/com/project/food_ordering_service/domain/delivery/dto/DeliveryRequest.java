package com.project.food_ordering_service.domain.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {

    private Long orderId;
    private Long riderId;
}
