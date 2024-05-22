package com.project.food_ordering_service.domain.user.service;

import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.exception.DuplicatedEmailException;
import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.exception.DuplicatedLoginIdException;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Service : 해당 클래스가 비즈니스 로직을 수행하는 서비스 클래스임을 나타내는 애노테이션
 * @RequiredArgsConstructor : final 필드의 생성자를 생성해주는 애노테이션
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(UserSaveRequest userSaveRequest) {
        if (userRepository.existsByEmail(userSaveRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }

        if (userRepository.existsByLoginId(userSaveRequest.getLoginId())) {
            throw new DuplicatedLoginIdException();
        }

        String encodedPassword = passwordEncoder.encode(userSaveRequest.getPassword());
        userSaveRequest.setPassword(encodedPassword);

        User savedUser = userRepository.save(userSaveRequest.toEntity());
        return savedUser;
    }
}
