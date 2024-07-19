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

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private User rider;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void updateDeliveryStatus(OrderStatus orderStatus) { // 배달 상황에 따라 배달 시작 시간, 완료 시간 저장
        this.status = orderStatus;

        if (orderStatus == OrderStatus.ONTHEWAY) {
            this.startedAt = LocalDateTime.now();
        } else if (orderStatus == OrderStatus.DELIVERED) {
            this.completedAt = LocalDateTime.now();
        }
    }
}
