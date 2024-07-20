package com.project.food_ordering_service.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPatchRequest {

    @NotNull
    private String loginId;

    @NotNull
    private String userName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String modifiedBy;
}