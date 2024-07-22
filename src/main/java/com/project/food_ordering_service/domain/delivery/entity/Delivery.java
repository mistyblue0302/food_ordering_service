package com.project.food_ordering_service.domain.delivery.entity;

import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "DELIVERIES")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "rider_id")
    private User rider;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void startDelivery(OrderStatus status) {
        this.startedAt = LocalDateTime.now();
        this.order.updateOrderStatus(status);
    }

    public void completeDelivery(OrderStatus status) {
        this.completedAt = LocalDateTime.now();
        this.order.updateOrderStatus(status);
    }
}
