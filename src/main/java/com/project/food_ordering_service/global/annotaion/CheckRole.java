package com.project.food_ordering_service.global.annotaion;

import com.project.food_ordering_service.domain.user.entity.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target : 애노테이션을 적용할 위치 선택
 * @Retention : 애노테이션이 어느 시점까지 영향을 미치는지 결정
 * 애노테이션 정보를 런타임까지 유지합니다. 이유는 스프링 AOP에서 이 정보를 사용할 수 있도록 하기 위함입니다.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
    // 해당 애노테이션을 사용하는 메소드에서 요구되는 Role을 지정합니다.
    Role requiredRole();
}

