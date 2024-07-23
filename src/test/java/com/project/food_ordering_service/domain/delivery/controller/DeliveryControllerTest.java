package com.project.food_ordering_service.domain.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.food_ordering_service.domain.delivery.dto.DeliveryRequest;
import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.delivery.service.DeliveryService;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.global.utils.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeliveryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DeliveryService deliveryService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("배달 상태 변경 테스트")
    void assignDelivery() throws Exception {
        // given
        Long orderId = 1L;
        Long riderId = 1L;

        DeliveryRequest deliveryRequest = new DeliveryRequest(orderId, riderId);
        Delivery delivery = Delivery.builder()
                .id(1L)
                .order(new Order())
                .rider(new User())
                .build();

        when(deliveryService.assignDelivery(anyLong(), anyLong(), any())).thenReturn(delivery);

        String token = jwtUtil.createAccessToken(riderId, Role.RIDER);

        Jws<Claims> claimsJws = jwtUtil.parseToken(token);
        System.out.println("Claims: " + claimsJws.getBody());

        // when
        mockMvc.perform(post("/deliveries/{orderId}/assign", orderId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryRequest)))
                // then
                .andExpect(status().isOk());
    }
}
