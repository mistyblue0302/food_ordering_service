package com.project.food_ordering_service.domain.user.repository;

import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @DataJpaTest : JPA 테스트를 위한 애노테이션으로, 레포지토리 계층에 대한 격리된 테스트 환경을 설정
 * 기본적으로 H2 데이터베이스를 인메모리 모드로 사용
 */

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("n+1 문제 발생 테스트")
    public void test() {
        List<User> users = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            User user = User.builder()
                    .loginId("user" + i)
                    .userName("User " + i)
                    .role(Role.CLIENT)
                    .build();
            users.add(user);
            entityManager.persist(user);

            for (int j = 1; j <= 2; j++) {
                Order order = Order.builder()
                        .customerName("Client " + ((i - 1) * 2 + j))
                        .user(user)
                        .status(OrderStatus.ORDERED)
                        .build();
                orders.add(order);
                entityManager.persist(order);
            }
        }

        entityManager.flush();
        entityManager.clear();

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            for (Order order : user.getOrders()) { // 여기서 추가 쿼리 발생 (N+1 문제)
                System.out.println("Order: " + order.getId());
            }
        }
    }
}