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
import com.project.food_ordering_service.global.annotaion.CheckRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    @CheckRole(requiredRole = Role.RIDER)
    public Delivery assignDelivery(Long orderId, Long riderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() != OrderStatus.DELIVERY_REQUESTED) {
            throw new IllegalStateException("배달 요청 상태가 올바르지 않습니다.");
        }

        User rider = userRepository.findById(riderId)
                .orElseThrow(UserNotFoundException::new);

        order.updateOrderStatus(OrderStatus.RECEIVED);

        Delivery delivery = Delivery.builder()
                .order(order)
                .rider(rider)
                .build();

        deliveryRepository.save(delivery);
        orderRepository.save(order);

        return delivery;
    }

    @Transactional
    @CheckRole(requiredRole = Role.RIDER)
    public Delivery updateDeliveryStatus(Long deliveryId, OrderStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        Order order = delivery.getOrder();

        if (status == OrderStatus.ONTHEWAY) {
            delivery.startDelivery(status);
        } else if (status == OrderStatus.DELIVERED) {
            delivery.completeDelivery(status);
        } else {
            throw new IllegalArgumentException("잘못된 주문 상태입니다.");
        }

        deliveryRepository.save(delivery);

        if (status == OrderStatus.DELIVERED) {
            order.updateOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
        }

        return delivery;
    }

    @Transactional
    public void cancelDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        Order order = delivery.getOrder();

        if (order.getStatus() != OrderStatus.RECEIVED) {
            throw new IllegalStateException("배달 상태가 RECEIVED일 때만 배달을 취소할 수 있습니다.");
        }

        order.updateOrderStatus(OrderStatus.DELIVERY_REQUESTED);

        deliveryRepository.delete(delivery);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryResponse> getDeliveries(Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
        return deliveries.map(DeliveryResponse::from);
    }
}