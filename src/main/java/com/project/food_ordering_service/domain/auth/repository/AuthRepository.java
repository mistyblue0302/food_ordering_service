package com.project.food_ordering_service.domain.auth.repository;

import com.project.food_ordering_service.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<RefreshToken, Long> {
}
