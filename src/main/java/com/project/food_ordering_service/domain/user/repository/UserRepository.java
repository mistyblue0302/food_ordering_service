package com.project.food_ordering_service.domain.user.repository;

import com.project.food_ordering_service.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByLoginId(String loginId);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    @Query("select u from User u join fetch u.orders o join fetch o.delivery d")
    List<User> findAllWithOrdersAndDeliveries();
}
