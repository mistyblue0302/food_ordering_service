package com.project.food_ordering_service.global.aop;

import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.global.annotaion.CheckRole;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    // @CheckRole 애노테이션이 붙은 메소드가 호출되면 해당 애노테이션의 속성인 requiredRole을 가져와 현재 사용자의 역할과 비교합니다.
    @Around("execution(@com.project.food_ordering_service.global.annotaion.CheckRole * *(..)) && @annotation(checkRole)")
    public Object checkUserRole(ProceedingJoinPoint joinPoint, CheckRole checkRole) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();

        Role requiredRole = checkRole.requiredRole();

        if (!jwtAuthentication.getRole().equals(requiredRole)) {
            throw new AccessDeniedException("접근이 거부되었습니다.");
        }

        return joinPoint.proceed();
    }
}