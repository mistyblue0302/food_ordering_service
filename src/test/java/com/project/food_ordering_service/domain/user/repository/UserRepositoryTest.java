package com.project.food_ordering_service.domain.user.repository;

import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @DataJpaTest : JPA 테스트를 위한 애노테이션으로, 레포지토리 계층에 대한 격리된 테스트 환경을 설정.
 * 기본적으로 H2 데이터베이스를 인메모리 모드로 사용.
 */

@DataJpaTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("N+1 발생 테스트")
    void testNPlusOne() {
        // given
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .userName("User " + i)
                    .email(i + "@google.com")
                    .role(Role.CLIENT)
                    .build();
            users.add(user);
        }
        users = userRepository.saveAll(users);

        List<Order> orders = new ArrayList<>();
        for (User user : users) {
            for (int j = 0; j < 3; j++) {
                Order order = Order.builder()
                        .customerName("Client " + j)
                        .user(user)
                        .build();
                orders.add(order);
            }
        }
        orderRepository.saveAll(orders);

        // when, then
        List<User> userList = userRepository.findAll();
    }
}