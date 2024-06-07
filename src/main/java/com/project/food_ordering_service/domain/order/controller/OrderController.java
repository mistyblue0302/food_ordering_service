package com.project.food_ordering_service.domain.order.controller;

import com.project.food_ordering_service.domain.order.dto.OrderRequest;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.service.OrderService;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
        @AuthenticationPrincipal JwtAuthentication jwtAuthentication,
        @RequestBody @Validated OrderRequest orderRequest) {
        Order order = orderService.createOrder(jwtAuthentication, orderRequest);
        return ResponseEntity.ok(order);
    }
}
