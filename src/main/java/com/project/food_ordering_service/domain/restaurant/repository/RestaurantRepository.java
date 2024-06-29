package com.project.food_ordering_service.domain.restaurant.repository;

import com.project.food_ordering_service.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
