package com.project.food_ordering_service.domain.order.dto;

import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.restaurant.dto.RestaurantResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<OrderResponse> getOrderResponse(Order order) {
        RestaurantResponse restaurantResponse = RestaurantResponse.builder()
                .id(order.getRestaurant().getId())
                .name(order.getRestaurant().getName())
                .address(order.getRestaurant().getAddress())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .customerName(order.getCustomerName())
                .deliveryAddress(order.getDeliveryAddress())
                .customerPhone(order.getCustomerPhone())
                .status(order.getStatus())
                .restaurantResponse(restaurantResponse)
                .build();

        return ResponseEntity.ok(orderResponse);
    }

}
