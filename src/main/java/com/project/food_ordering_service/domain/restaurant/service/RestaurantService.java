package com.project.food_ordering_service.domain.restaurant.service;

import com.project.food_ordering_service.global.api.KakaoMapApi;
import com.project.food_ordering_service.global.dto.request.KakaoMapApiRequest;
import com.project.food_ordering_service.global.dto.response.KakaoMapApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final KakaoMapApi kakaoMapApi;

    @Transactional(readOnly = true)
    public KakaoMapApiResponse getRestaurant(KakaoMapApiRequest kakaoMapApiRequest) {
        return kakaoMapApi.getSearchPlaceByKeyword(kakaoMapApiRequest).getBody();
    }
}