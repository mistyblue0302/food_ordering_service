package com.project.food_ordering_service.domain.order.dto;

import com.project.food_ordering_service.domain.restaurant.dto.RestaurantRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String customerName;

    private String deliveryAddress;

    private String customerPhone;

    private RestaurantRequest restaurantRequest;
}
