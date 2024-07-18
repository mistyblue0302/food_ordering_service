package com.project.food_ordering_service.global.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoMapApiResponse {

    private Meta meta;

    private List<Documents> documents;
}
