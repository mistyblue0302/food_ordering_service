package com.project.food_ordering_service.domain.order.repository;

import com.project.food_ordering_service.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    @Query("select o from Order o join fetch o.delivery d")
    List<Order> findAllWithDeliveries();
}