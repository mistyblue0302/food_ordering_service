package com.project.food_ordering_service.domain.user.repository;

import com.project.food_ordering_service.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByLoginId(String loginId);
}
