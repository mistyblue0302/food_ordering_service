package com.project.food_ordering_service.domain.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.food_ordering_service.domain.order.dto.OrderRequest;
import com.project.food_ordering_service.domain.order.dto.OrderStateRequest;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.restaurant.dto.RestaurantRequest;
import com.project.food_ordering_service.domain.restaurant.entity.Restaurant;
import com.project.food_ordering_service.domain.restaurant.repository.RestaurantRepository;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.domain.utils.TestUtil;
import com.project.food_ordering_service.global.exception.CustomException;
import com.project.food_ordering_service.global.exception.ErrorInformation;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import com.project.food_ordering_service.global.utils.jwt.JwtHolder;
import com.project.food_ordering_service.global.utils.jwt.JwtProperties;
import com.project.food_ordering_service.global.utils.sse.service.SseService;
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

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    SseService sseService;

    User savedUser;

    OrderRequest orderRequest;

    Restaurant savedRestaurant;


    @BeforeEach
    void setUp() {
        savedUser = TestUtil.savedUser;

        savedRestaurant = Restaurant.builder()
                .id(TestUtil.RESTAURANT_ID)
                .name("Test Restaurant")
                .address("Test Address")
                .build();

        RestaurantRequest restaurantRequest = RestaurantRequest.builder()
                .name("Test Restaurant")
                .address("Test Address")
                .build();

        orderRequest = OrderRequest.builder()
                .restaurantRequest(restaurantRequest)
                .build();
    }

    @Test
    @DisplayName("주문 성공 테스트")
    void order_success() {
        // given
        JwtHolder jwtHolder = new JwtHolder(
                createMockClaims(TestUtil.USER_ID, "testToken", JwtProperties.ACCESS_TOKEN_NAME,
                        Role.CLIENT), "testToken");
        JwtAuthentication jwtAuthentication = new JwtAuthentication(jwtHolder);

        when(userRepository.findById(TestUtil.USER_ID)).thenReturn(Optional.of(TestUtil.savedUser));

        Restaurant savedRestaurant = Restaurant.builder()
                .id(TestUtil.RESTAURANT_ID)
                .name("Test Restaurant")
                .address("Test Street")
                .build();

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);
        when(orderRepository.save(any(Order.class))).thenReturn(
                Order.builder().user(TestUtil.savedUser).restaurant(savedRestaurant)
                        .status(OrderStatus.ORDERED).build());

        // when
        Order createdOrder = orderService.createOrder(jwtAuthentication, orderRequest);

        // then
        assertNotNull(createdOrder);
        assertEquals(TestUtil.savedUser, createdOrder.getUser());
        assertEquals(savedRestaurant, createdOrder.getRestaurant());
        assertEquals(OrderStatus.ORDERED, createdOrder.getStatus());
    }

    @Test
    @DisplayName("주문 실패 테스트")
    void order_failed() {
        // given
        JwtHolder jwtHolder = new JwtHolder(
                createMockClaims(TestUtil.USER_ID, "testToken", JwtProperties.ACCESS_TOKEN_NAME,
                        Role.CLIENT), "testToken");
        JwtAuthentication jwtAuthentication = new JwtAuthentication(jwtHolder);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.createOrder(jwtAuthentication, orderRequest);
        });

        assertEquals(ErrorInformation.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("주문 상태 전환 성공 테스트 : 주문 완료 -> 준비 완료")
    void updateOrderStatusSuccess() {
        // given
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .user(savedUser)
                .restaurant(savedRestaurant)
                .status(OrderStatus.ORDERED)
                .build();

        OrderStateRequest orderStateRequest = OrderStateRequest.builder()
                .status(OrderStatus.PREPARED)
                .build();

        JwtAuthentication jwtAuthentication = createJwtAuthentication(TestUtil.USER_ID, Role.OWNER);

        // when
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(jwtAuthentication, orderId,
                orderStateRequest);

        // then
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.PREPARED, updatedOrder.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("주문 상태 전환 성공 테스트 : 준비 완료 -> 배달 요청")
    void updateOrderStatusSuccess2() {
        // given
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .user(savedUser)
                .restaurant(savedRestaurant)
                .status(OrderStatus.PREPARED)
                .build();

        OrderStateRequest orderStateRequest = OrderStateRequest.builder()
                .status(OrderStatus.DELIVERY_REQUESTED)
                .build();

        JwtAuthentication jwtAuthentication = createJwtAuthentication(TestUtil.USER_ID, Role.OWNER);

        // when
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(jwtAuthentication, orderId,
                orderStateRequest);

        // then
        assertEquals(OrderStatus.DELIVERY_REQUESTED, updatedOrder.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("주문 상태 전환 실패 테스트")
    void updateOrderStatusFailed() {
        // given
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .user(savedUser)
                .restaurant(savedRestaurant)
                .status(OrderStatus.ORDERED)
                .build();

        OrderStateRequest stateRequest = OrderStateRequest.builder()
                .status(OrderStatus.DELIVERY_REQUESTED)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        JwtAuthentication jwtAuthentication = createJwtAuthentication(1L, Role.OWNER);

        // when, then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.updateOrderStatus(jwtAuthentication, orderId, stateRequest);
        });

        assertEquals(ErrorInformation.REQUEST_VALIDATION_FAIL.getMessage(), exception.getMessage());
    }

    private JwtAuthentication createJwtAuthentication(Long userId, Role role) {
        JwtHolder jwtHolder = new JwtHolder(
                createMockClaims(userId, "testToken", JwtProperties.ACCESS_TOKEN_NAME, role),
                "testToken");
        return new JwtAuthentication(jwtHolder);
    }

    private Jws<Claims> createMockClaims(Long userId, String token, String tokenType, Role role) {
        // Claims 객체를 통해 사용자 id를 포함하는 클레임 생성(클레임에 userId와 인자로 넘어온 id 추가)
        // 만료시간은 현재 시간에서 1시간을 더한 값으로 설정
        Claims claimsBody = new DefaultClaims();
        claimsBody.put(JwtProperties.USER_ID, userId.toString());
        claimsBody.put(JwtProperties.ROLE, role.name());
        claimsBody.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        // JwsHeader 객체를 통해 헤더를 생성(헤더에 토큰 타입 설정)
        JwsHeader header = new DefaultJwsHeader();
        header.setType(tokenType);

        return new DefaultJws<>(header, claimsBody, "signature");
    }
}