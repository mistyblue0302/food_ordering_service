package com.project.food_ordering_service.global.aop;

import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.global.annotaion.CheckRole;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private static final Logger logger = LoggerFactory.getLogger(RoleCheckAspect.class);

    // @CheckRole 애노테이션이 붙은 메소드가 호출되면 해당 애노테이션의 속성인 requiredRole을 가져와 현재 사용자의 역할과 비교합니다.
    @Around("execution(@com.project.food_ordering_service.global.annotaion.CheckRole * *(..)) && @annotation(checkRole)")
    public Object checkUserRole(ProceedingJoinPoint joinPoint, CheckRole checkRole) throws Throwable {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();

            Role requiredRole = checkRole.requiredRole();

            if (!jwtAuthentication.getRole().equals(requiredRole)) {
                throw new AccessDeniedException("접근이 거부되었습니다.");
            }

            return joinPoint.proceed();
        } catch (Exception e) {
            logger.error("예외 발생: {}", e.getMessage(), e);
            throw e;
        }
    }
}