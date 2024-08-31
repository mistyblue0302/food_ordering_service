package com.project.food_ordering_service.domain.order.service;

import com.project.food_ordering_service.domain.order.dto.OrderRequest;
import com.project.food_ordering_service.domain.order.dto.OrderResponse;
import com.project.food_ordering_service.domain.order.dto.OrderStateRequest;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.restaurant.entity.Restaurant;
import com.project.food_ordering_service.domain.restaurant.repository.RestaurantRepository;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.exception.CustomException;
import com.project.food_ordering_service.global.exception.ErrorInformation;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import com.project.food_ordering_service.global.utils.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final SseService sseService;

    @Transactional
    public Order createOrder(JwtAuthentication jwtAuthentication, OrderRequest orderRequest) {
        Long userId = jwtAuthentication.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorInformation.USER_NOT_FOUND));

        Restaurant restaurant = Restaurant.builder()
                .name(orderRequest.getRestaurantRequest().getName())
                .address(orderRequest.getRestaurantRequest().getAddress())
                .build();

        restaurantRepository.save(restaurant);

        Order order = Order.builder()
                .user(user)
                .customerName(orderRequest.getCustomerName())
                .deliveryAddress(orderRequest.getDeliveryAddress())
                .customerPhone(orderRequest.getCustomerPhone())
                .restaurant(restaurant)
                .status(OrderStatus.ORDERED)
                .build();

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(JwtAuthentication jwtAuthentication, Long orderId,
                                   OrderStateRequest stateRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorInformation.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.ORDERED
                && stateRequest.getStatus() == OrderStatus.PREPARED) {
            order.updateOrderStatus(stateRequest.getStatus());
        } else if (order.getStatus() == OrderStatus.PREPARED
                && stateRequest.getStatus() == OrderStatus.DELIVERY_REQUESTED) {
            order.updateOrderStatus(stateRequest.getStatus());
        } else {
            throw new CustomException(ErrorInformation.REQUEST_VALIDATION_FAIL);
        }

        sseService.sand(jwtAuthentication.getId(), OrderResponse.getOrderResponse(order));
        return orderRepository.save(order);
    }

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersByUser(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    public List<Order> getOrdersWithDeliveries() {
        return orderRepository.findAllWithDeliveries();
    }
}