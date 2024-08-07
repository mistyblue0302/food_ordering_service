package com.project.food_ordering_service.domain.delivery.dto;

import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class DeliveryResponse {

    private Long orderId;
    private Long riderId;
    private OrderStatus orderStatus;
    private String deliveryAddress;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public static DeliveryResponse from(Delivery delivery) {
        return DeliveryResponse.builder()
                .orderId(delivery.getOrder().getId())
                .riderId(delivery.getRider().getId())
                .orderStatus(delivery.getOrder().getStatus())
                .deliveryAddress(delivery.getOrder().getDeliveryAddress())
                .startedAt(delivery.getStartedAt())
                .completedAt(delivery.getCompletedAt())
                .build();
    }
}
