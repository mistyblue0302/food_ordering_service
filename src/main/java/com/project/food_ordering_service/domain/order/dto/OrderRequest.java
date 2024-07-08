package com.project.food_ordering_service.domain.order.dto;

import com.project.food_ordering_service.domain.restaurant.dto.RestaurantRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequest {

    private String customerName;

    private String deliveryAddress;

    private String customerPhone;

    private RestaurantRequest restaurantRequest;
}
