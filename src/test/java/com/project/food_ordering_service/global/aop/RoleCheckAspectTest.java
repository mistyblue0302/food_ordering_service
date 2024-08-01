package com.project.food_ordering_service.global.aop;

import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.global.annotaion.CheckRole;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoleCheckAspectTest {

    @InjectMocks
    private RoleCheckAspect roleCheckAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthentication jwtAuthentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("CLIENT 권한을 가진 사용자가 주문을 할 때 성공 테스트")
    void clientRoleSuccess() throws Throwable {
        // given
        CheckRole checkRole = mock(CheckRole.class);
        when(checkRole.requiredRole()).thenReturn(Role.CLIENT);

        when(authentication.getPrincipal()).thenReturn(jwtAuthentication);
        when(jwtAuthentication.getRole()).thenReturn(Role.CLIENT);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // joinPoint.proceed()가 호출될 때 문자열 반환 -> 실제 메소드 실행이 성공적으로 완료될 때 반환값을 세팅
        when(joinPoint.proceed()).thenReturn("Success");

        // when
        Object result = roleCheckAspect.checkUserRole(joinPoint, checkRole); // 애노테이션 역할 검사 후 결과 저장

        // then
        assertEquals("Success", result);
    }

    @Test
    @DisplayName("권한이 없는 사용자 예외 발생 테스트")
    void testRoleCheckAspectWithInvalidRole() throws Throwable {
        // given
        CheckRole checkRole = mock(CheckRole.class);
        when(checkRole.requiredRole()).thenReturn(Role.CLIENT);

        when(authentication.getPrincipal()).thenReturn(jwtAuthentication);
        when(jwtAuthentication.getRole()).thenReturn(Role.ADMIN);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when, then
        assertThrows(AccessDeniedException.class,
                () -> roleCheckAspect.checkUserRole(joinPoint, checkRole)
        );
    }
}