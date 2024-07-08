package com.project.food_ordering_service.domain.order.dto;

import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.restaurant.dto.RestaurantResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {

    private Long id;

    private Long userId;

    private String customerName;

    private String deliveryAddress;

    private String customerPhone;

    private OrderStatus status;
    
    private RestaurantResponse restaurantResponse;
}
