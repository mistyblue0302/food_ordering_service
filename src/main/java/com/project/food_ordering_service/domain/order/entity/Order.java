package com.project.food_ordering_service.domain.order.entity;

import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.restaurant.entity.Restaurant;
import com.project.food_ordering_service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ORDERS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String deliveryAddress;

    private String customerPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Delivery delivery;

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }
}