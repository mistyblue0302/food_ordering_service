package com.project.food_ordering_service.domain.order.service;

import static com.project.food_ordering_service.domain.utils.TestUtil.RESTAURANT_ID;
import static com.project.food_ordering_service.domain.utils.TestUtil.USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.food_ordering_service.domain.order.dto.OrderRequest;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.domain.utils.TestUtil;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import com.project.food_ordering_service.global.utils.jwt.JwtHolder;
import com.project.food_ordering_service.global.utils.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    User savedUser;
    OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        savedUser = TestUtil.savedUser;
        orderRequest = OrderRequest.builder()
            .restaurantId(RESTAURANT_ID)
            .build();
    }

    @Test
    @DisplayName("주문 성공 테스트")
    void order_success() {
        //given
        JwtAuthentication jwtAuthentication = new JwtAuthentication(
            new JwtHolder(createMockClaims(USER_ID, "testToken", JwtProperties.ACCESS_TOKEN_NAME),
                "testToken"));

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(savedUser));
        when(orderRepository.save(any(Order.class))).thenReturn(
            Order.builder().user(savedUser).restaurantId(RESTAURANT_ID).status(OrderStatus.ORDERED)
                .build());

        // when
        Order createdOrder = orderService.createOrder(jwtAuthentication, orderRequest);

        // then
        assertNotNull(createdOrder);
        assertEquals(savedUser, createdOrder.getUser());
        assertEquals(RESTAURANT_ID, createdOrder.getRestaurantId());
        assertEquals(OrderStatus.ORDERED, createdOrder.getStatus());
    }

    private Jws<Claims> createMockClaims(Long userId, String token, String tokenType) {
        // Claims 객체를 통해 사용자 id를 포함하는 클레임 생성(클레임에 userId와 인자로 넘어온 id 추가)
        // 만료시간은 현재 시간에서 1시간을 더한 값으로 설정
        Claims claimsBody = new DefaultClaims();
        claimsBody.put(JwtProperties.USER_ID, userId.toString());
        claimsBody.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        // JwsHeader 객체를 통해 헤더를 생성(헤더에 토큰 타입 설정)
        JwsHeader header = new DefaultJwsHeader();
        header.setType(tokenType);

        return new DefaultJws<>(header, claimsBody, "signature");
    }
}