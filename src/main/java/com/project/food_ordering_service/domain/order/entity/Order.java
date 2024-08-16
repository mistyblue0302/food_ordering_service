package com.project.food_ordering_service.domain.order.entity;

import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.restaurant.entity.Restaurant;
import com.project.food_ordering_service.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "order")
    private Delivery delivery;

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }
}