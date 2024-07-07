package com.project.food_ordering_service.domain.order.dto;

import com.project.food_ordering_service.domain.restaurant.dto.RestaurantResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryResponse {


    private final RestaurantResponse restaurant;

}
