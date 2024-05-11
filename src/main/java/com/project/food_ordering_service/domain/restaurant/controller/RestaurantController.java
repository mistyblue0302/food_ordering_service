package com.project.food_ordering_service.domain.restaurant.controller;

import com.project.food_ordering_service.global.api.KakaoMapApi;
import com.project.food_ordering_service.global.dto.request.KakaoMapApiRequest;
import com.project.food_ordering_service.global.dto.response.KakaoMapApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RestaurantController {

    private final KakaoMapApi kakaoMapApi;

    @PostMapping("/restaurant")
    public ResponseEntity<KakaoMapApiResponse> getRestaurant(
        @RequestBody KakaoMapApiRequest kakaoMapApiRequest) {
        ResponseEntity<KakaoMapApiResponse> response = kakaoMapApi.getSearchPlaceByKeyword(
            kakaoMapApiRequest);
        return ResponseEntity.ok().body(response.getBody());
    }
}
