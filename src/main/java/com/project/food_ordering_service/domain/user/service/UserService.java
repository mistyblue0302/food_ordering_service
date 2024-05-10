package com.project.food_ordering_service.domain.user.service;

import com.project.food_ordering_service.domain.user.dto.UserSaveResponse;
import com.project.food_ordering_service.domain.user.entity.User;
import com.project.food_ordering_service.domain.user.exception.DuplicatedEmailException;
import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.exception.DuplicatedLoginIdException;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User addUser(UserSaveRequest userSaveRequest) {
        if (userRepository.existsByEmail(userSaveRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }

        if (userRepository.existsByLoginId(userSaveRequest.getLoginId())) {
            throw new DuplicatedLoginIdException();
        }

        User savedUser = userRepository.save(userSaveRequest.toEntity());
        return savedUser;
    }
}
