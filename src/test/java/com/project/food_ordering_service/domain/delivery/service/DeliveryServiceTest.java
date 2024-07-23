package com.project.food_ordering_service.domain.delivery.service;

import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.delivery.repository.DeliveryRepository;
import com.project.food_ordering_service.domain.order.entity.Order;
import com.project.food_ordering_service.domain.order.entity.OrderStatus;
import com.project.food_ordering_service.domain.order.repository.OrderRepository;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import com.project.food_ordering_service.global.utils.jwt.JwtHolder;
import com.project.food_ordering_service.global.utils.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    DeliveryService deliveryService;

    @Mock
    DeliveryRepository deliveryRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    User savedRider;
    User savedUser;
    Order savedOrder;
    Delivery savedDelivery;

    @BeforeEach
    void setUp() {
        savedRider = User.builder().id(1L).role(Role.RIDER).build();
        savedUser = User.builder().id(2L).build();

        savedOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.DELIVERY_REQUESTED)
                .build();

        savedDelivery = Delivery.builder()
                .id(1L)
                .order(savedOrder)
                .rider(savedRider)
                .build();
    }

    @Test
    @DisplayName("배달 할당 성공 테스트")
    void assignDeliverySuccess() {
        // given
        JwtAuthentication jwtAuthentication = createJwtAuthentication(savedRider.getId(), Role.RIDER);

        when(orderRepository.findById(savedOrder.getId())).thenReturn(Optional.of(savedOrder));
        when(userRepository.findById(savedRider.getId())).thenReturn(Optional.of(savedRider));

        // when
        Delivery assignedDelivery = deliveryService.assignDelivery(savedOrder.getId(), savedRider.getId(), jwtAuthentication);

        // then
        assertEquals(OrderStatus.RECEIVED, savedOrder.getStatus());
        verify(deliveryRepository).save(assignedDelivery);
        verify(orderRepository).save(savedOrder);
    }

    @Test
    @DisplayName("배달 할당 실패 테스트 : 권한이 배달원이 아닐 때")
    void assignDeliveryFailedDueToPermission() {
        // given
        JwtAuthentication jwtAuthentication = createJwtAuthentication(savedRider.getId(), Role.CLIENT);

        // when, then
        assertThrows(AccessDeniedException.class, () -> {
            deliveryService.assignDelivery(savedOrder.getId(), savedRider.getId(), jwtAuthentication);
        });
    }

    @Test
    @DisplayName("배달 상태 업데이트 성공 테스트 : 배달 중")
    void updateDeliveryStatusToOnTheWay() {
        // given
        JwtAuthentication jwtAuthentication = createJwtAuthentication(savedRider.getId(), Role.RIDER);
        savedDelivery = Delivery.builder()
                .id(1L)
                .order(savedOrder)
                .rider(savedRider)
                .startedAt(LocalDateTime.now())
                .build();

        when(deliveryRepository.findById(savedDelivery.getId())).thenReturn(Optional.of(savedDelivery));

        // when
        Delivery updatedDelivery = deliveryService.updateDeliveryStatus(savedDelivery.getId(), OrderStatus.ONTHEWAY, jwtAuthentication);

        // then
        assertEquals(OrderStatus.ONTHEWAY, updatedDelivery.getOrder().getStatus());
        assertNotNull(updatedDelivery.getStartedAt());
        verify(deliveryRepository).save(updatedDelivery);
    }

    @Test
    @DisplayName("배달 상태 업데이트 성공 테스트 : 배달 완료")
    void updateDeliveryStatusToDelivered() {
        // given
        JwtAuthentication jwtAuthentication = createJwtAuthentication(savedRider.getId(), Role.RIDER);
        savedDelivery = Delivery.builder()
                .id(1L)
                .order(savedOrder)
                .rider(savedRider)
                .startedAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .build();

        when(deliveryRepository.findById(savedDelivery.getId())).thenReturn(Optional.of(savedDelivery));

        // when
        Delivery updatedDelivery = deliveryService.updateDeliveryStatus(savedDelivery.getId(), OrderStatus.DELIVERED, jwtAuthentication);

        // then
        assertEquals(OrderStatus.DELIVERED, updatedDelivery.getOrder().getStatus());
        assertNotNull(updatedDelivery.getCompletedAt());
        verify(deliveryRepository).save(updatedDelivery);
    }

    private JwtAuthentication createJwtAuthentication(Long userId, Role role) {
        JwtHolder jwtHolder = new JwtHolder(
                createMockClaims(userId, "testToken", JwtProperties.ACCESS_TOKEN_NAME, role),
                "testToken");
        return new JwtAuthentication(jwtHolder);
    }

    private Jws<Claims> createMockClaims(Long userId, String token, String tokenType, Role role) {
        Claims claimsBody = new DefaultClaims();
        claimsBody.put(JwtProperties.USER_ID, userId.toString());
        claimsBody.put(JwtProperties.ROLE, role.name());
        claimsBody.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        JwsHeader header = new DefaultJwsHeader();
        header.setType(tokenType);

        return new DefaultJws<>(header, claimsBody, "signature");
    }
}