package com.project.food_ordering_service.domain.order.service;

import com.project.food_ordering_service.domain.order.dto.OrderRequest;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.exception.UserNotFoundException;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public Order createOrder(JwtAuthentication jwtAuthentication, OrderRequest orderRequest) {
        Long userId = jwtAuthentication.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Order order = Order.builder()
                .user(user)
                .restaurantId(orderRequest.getRestaurantId())
                .status(OrderStatus.ORDERED)
                .build();

        return orderRepository.save(order);
    }

    public Page<Order> getOrders(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest);
    }

    public Page<Order> getOrdersByUser(Long userId, PageRequest pageRequest) {
        return orderRepository.findByUserId(userId, pageRequest);
    }
}
