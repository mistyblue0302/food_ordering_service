package com.project.food_ordering_service.global.kakaoRestApi.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseKakaoApi {

    private Meta meta;

    private List<Documents> documents;

}
