package com.project.food_ordering_service.domain.delivery.service;

import com.project.food_ordering_service.domain.delivery.dto.DeliveryResponse;
import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.delivery.repository.DeliveryRepository;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.annotaion.DistributedLock;
import com.project.food_ordering_service.global.exception.CustomException;
import com.project.food_ordering_service.global.exception.ErrorInformation;
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

    @DistributedLock(key = "#orderId")
    public Delivery assignDelivery(Long orderId, Long riderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorInformation.ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.DELIVERY_REQUESTED) {
            throw new CustomException(ErrorInformation.REQUEST_VALIDATION_FAIL);
        }

        User rider = userRepository.findById(riderId)
                .orElseThrow(() -> new CustomException(ErrorInformation.USER_NOT_FOUND));

        // 예외 처리 나중에 수정
        if (deliveryRepository.existsByRiderAndOrderStatusNot(rider, OrderStatus.DELIVERED)) {
            throw new CustomException(ErrorInformation.REQUEST_VALIDATION_FAIL);
        }

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
    public Delivery updateDeliveryStatus(Long deliveryId, OrderStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(ErrorInformation.DELIVERY_NOT_FOUND));

        if (status == OrderStatus.ONTHEWAY) {
            delivery.startDelivery(status);
        } else if (status == OrderStatus.DELIVERED) {
            delivery.completeDelivery(status);
        } else {
            throw new CustomException(ErrorInformation.REQUEST_VALIDATION_FAIL);
        }

        deliveryRepository.save(delivery);

        return delivery;
    }

    @Transactional
    public void cancelDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(ErrorInformation.DELIVERY_NOT_FOUND));

        Order order = delivery.getOrder();

        if (order.getStatus() != OrderStatus.RECEIVED) {
            throw new CustomException(ErrorInformation.REQUEST_VALIDATION_FAIL);
        }

        order.updateOrderStatus(OrderStatus.DELIVERY_REQUESTED);

        deliveryRepository.delete(delivery);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(ErrorInformation.DELIVERY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<DeliveryResponse> getDeliveries(Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
        return deliveries.map(DeliveryResponse::from);
    }
}