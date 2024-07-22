package com.project.food_ordering_service.domain.order.controller;

import com.project.food_ordering_service.domain.order.dto.OrderRequest;
import com.project.food_ordering_service.domain.order.dto.OrderResponse;
import com.project.food_ordering_service.domain.order.dto.OrderStateRequest;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.service.OrderService;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication,
            @RequestBody @Validated OrderRequest orderRequest) {
        Order order = orderService.createOrder(jwtAuthentication, orderRequest);

        return OrderResponse.getOrderResponse(order);
    }

    @PatchMapping("/{orderId}/state")
    public ResponseEntity<Order> updateOrderStatus(
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication,
            @PathVariable Long orderId,
            @RequestBody OrderStateRequest stateRequest) {
        Order order = orderService.updateOrderStatus(jwtAuthentication, orderId, stateRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Order> orders = orderService.getOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Order>> getOrdersByUser(
            @PathVariable Long userId,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Order> orders = orderService.getOrdersByUser(userId, pageable);
        return ResponseEntity.ok(orders);
    }
}
