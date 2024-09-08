package com.project.food_ordering_service.domain.restaurant.service;

import com.project.food_ordering_service.global.api.KakaoMapApi;
import com.project.food_ordering_service.global.dto.request.KakaoMapApiRequest;
import com.project.food_ordering_service.global.dto.response.KakaoMapApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final KakaoMapApi kakaoMapApi;

    public ResponseEntity<KakaoMapApiResponse> getRestaurant(
            @RequestBody KakaoMapApiRequest kakaoMapApiRequest) {
        ResponseEntity<KakaoMapApiResponse> response = kakaoMapApi.getSearchPlaceByKeyword(
                kakaoMapApiRequest);
        return ResponseEntity.ok().body(response.getBody());
    }
}
