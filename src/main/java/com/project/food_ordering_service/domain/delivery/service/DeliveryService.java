package com.project.food_ordering_service.domain.delivery.service;

import com.project.food_ordering_service.domain.delivery.dto.DeliveryResponse;
import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.delivery.exception.DeliveryNotFoundException;
import com.project.food_ordering_service.domain.delivery.repository.DeliveryRepository;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.exception.OrderNotFoundException;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.exception.UserNotFoundException;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public Delivery assignDelivery(Long orderId, Long riderId, JwtAuthentication jwtAuthentication) {
        if (!jwtAuthentication.getRole().equals(Role.RIDER)) {
            throw new AccessDeniedException("배달원만 배달을 할 수 있습니다.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() != OrderStatus.DELIVERY_REQUESTED) {
            throw new IllegalStateException("배달 요청 상태가 올바르지 않습니다.");
        }

        User rider = userRepository.findById(riderId)
                .orElseThrow(UserNotFoundException::new);

        if (rider.getRole() != Role.RIDER) {
            throw new IllegalArgumentException("배달원만 배달을 할 수 있습니다.");
        }

        Delivery delivery = Delivery.builder()
                .order(order)
                .rider(rider)
                .status(OrderStatus.COMPLETED) // 배달원이 배달을 수락한 상태
                .build();

        order.updateOrderStatus(OrderStatus.ONTHEWAY);

        deliveryRepository.save(delivery);
        orderRepository.save(order);

        return delivery;
    }

    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, OrderStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        if (status == OrderStatus.ONTHEWAY && delivery.getStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException("배달원이 배달을 수락하지 않았습니다.");
        }

        if (status != OrderStatus.DELIVERED && delivery.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("이미 배달 완료 상태입니다.");
        }

        delivery.updateDeliveryStatus(status);
        delivery.getOrder().updateOrderStatus(status);

        deliveryRepository.save(delivery);
        orderRepository.save(delivery.getOrder());

        return delivery;
    }

    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);
    }

    public Page<DeliveryResponse> getDeliveries(Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
        return deliveries.map(DeliveryResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<Delivery> getDeliveriesByStatus(OrderStatus status, Pageable pageable) {
        return deliveryRepository.findByStatus(status, pageable);
    }
}
