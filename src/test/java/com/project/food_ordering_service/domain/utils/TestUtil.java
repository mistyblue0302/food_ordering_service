package com.project.food_ordering_service.domain.utils;

import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.domain.user.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestUtil {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static final Long USER_ID = 1L;
    public static final String EMAIL = "test@gmail.com";
    public static final String PASSWORD = "test1234!";
    public static final String NICKNAME = "testNickname";
    public static final String PHONE_NUMBER = "01012341234";
    public static final Long RESTAURANT_ID = 2L;
    public static final Role ROLE_CLIENT = Role.CLIENT;
    public static final Role ROLE_OWNER = Role.OWNER;

    public static final User savedUser = User
            .builder()
            .id(1L)
            .password(bCryptPasswordEncoder.encode(PASSWORD))
            .phoneNumber(PHONE_NUMBER)
            .role(ROLE_CLIENT)
            .email(EMAIL)
            .build();
}
