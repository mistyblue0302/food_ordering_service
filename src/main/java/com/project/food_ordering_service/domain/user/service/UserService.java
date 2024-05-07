package com.project.food_ordering_service.domain.user.service;

import com.project.food_ordering_service.domain.user.exception.DuplicatedEmailException;
import com.project.food_ordering_service.domain.user.dto.UserSaveRequest;
import com.project.food_ordering_service.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void addUser(UserSaveRequest userSaveRequest) {
        if(userRepository.existsByEmail(userSaveRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }
        userRepository.save(userSaveRequest.toEntity());
    }
}
