package com.project.food_ordering_service.domain.restaurant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantRequest {

    private final Long id;

    private final String name;

    private final String address;
}