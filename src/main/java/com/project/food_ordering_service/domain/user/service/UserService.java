package com.project.food_ordering_service.domain.user.service;

import com.project.food_ordering_service.domain.user.dto.UserPatchRequest;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import com.project.food_ordering_service.global.exception.CustomException;
import com.project.food_ordering_service.global.exception.ErrorInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public User addUser(UserSaveRequest userSaveRequest) {
        if (userRepository.existsByEmail(userSaveRequest.getEmail())) {
            throw new CustomException(ErrorInformation.DUPLICATE_MEMBER);
        }

        if (userRepository.existsByLoginId(userSaveRequest.getLoginId())) {
            throw new CustomException(ErrorInformation.DUPLICATE_MEMBER);
        }

        String encodedPassword = passwordEncoder.encode(userSaveRequest.getPassword());
        userSaveRequest.setPassword(encodedPassword);

        User savedUser = userRepository.save(userSaveRequest.toEntity());
        return savedUser;
    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorInformation.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorInformation.USER_NOT_FOUND));
    }

    @Transactional
    public User modifyUser(Long userId, UserPatchRequest userPatchRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorInformation.USER_NOT_FOUND));

        user.modify(userPatchRequest.getUserName(), userPatchRequest.getPhoneNumber(),
                userPatchRequest.getModifiedBy());

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getUsersWithOrdersAndDeliveries() {
        return userRepository.findAllWithOrdersAndDeliveries();
    }
}