package com.project.food_ordering_service.domain.delivery.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.food_ordering_service.domain.delivery.dto.DeliveryRequest;
import com.project.food_ordering_service.domain.delivery.repository.DeliveryRepository;
import com.project.food_ordering_service.domain.delivery.service.DeliveryService;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.restaurant.entity.Restaurant;
import com.project.food_ordering_service.domain.restaurant.repository.RestaurantRepository;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.utils.jwt.JwtUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeliveryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("배달 할당 테스트")
    void assignDelivery() throws Exception {
        // given
        User customer = User.builder()
                .loginId("customerId")
                .userName("customerName")
                .role(Role.CLIENT)
                .createdAt(LocalDateTime.now())
                .build();

        User rider = User.builder()
                .loginId("riderId")
                .userName("riderName")
                .role(Role.RIDER)
                .createdAt(LocalDateTime.now())
                .build();

        customer = userRepository.save(customer);
        rider = userRepository.save(rider);

        Restaurant restaurant = Restaurant.builder()
                .name("testRestaurant")
                .address("testAddress")
                .build();

        restaurant = restaurantRepository.save(restaurant);

        Order order = Order.builder()
                .customerName("customerName")
                .deliveryAddress("deliveryAddress")
                .customerPhone("010-1234-5678")
                .user(customer)
                .restaurant(restaurant)
                .status(OrderStatus.DELIVERY_REQUESTED)
                .build();

        order = orderRepository.save(order);

        DeliveryRequest deliveryRequest = new DeliveryRequest(order.getId(), rider.getId());

        String token = jwtUtil.createAccessToken(rider.getId(), Role.RIDER);

        // when
        mockMvc.perform(post("/deliveries/assign", order.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryRequest)))
                // then
                .andExpect(status().isOk());
    }
}