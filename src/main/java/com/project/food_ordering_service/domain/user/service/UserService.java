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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<UserSaveResponse> addUser(UserSaveRequest userSaveRequest) {
        if(userRepository.existsByEmail(userSaveRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }

        if(userRepository.existsByLoginId(userSaveRequest.getLoginId())) {
            throw new DuplicatedLoginIdException();
        }

        User savedUser = userRepository.save(userSaveRequest.toEntity());
        UserSaveResponse response = UserSaveResponse.fromUser(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
