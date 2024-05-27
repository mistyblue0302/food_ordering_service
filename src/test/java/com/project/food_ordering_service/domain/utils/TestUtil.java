package com.project.food_ordering_service.domain.utils;

import com.project.food_ordering_service.domain.user.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestUtil {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static final Long USER_ID = 1L;
    public static final String EMAIL = "test@gmail.com";
    public static final String PASSWORD = "test1234!";
    public static final String NICKNAME = "testNickname";
    public static final String PHONE_NUMBER = "01012341234";
    public static final User savedUser = User
            .builder()
            .password(bCryptPasswordEncoder.encode(PASSWORD))
            .password(PHONE_NUMBER)
            .email(EMAIL)
            .build();
}
