package com.project.food_ordering_service.domain.restaurant.controller;

import com.project.food_ordering_service.global.kakaoRestApi.KakaoMapApi;
import com.project.food_ordering_service.global.kakaoRestApi.dto.RequestKakao;
import com.project.food_ordering_service.global.kakaoRestApi.dto.ResponseKakaoApi;
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
    public ResponseEntity<ResponseKakaoApi> getRestaurant(
        @RequestBody RequestKakao requestKakao) {
        ResponseEntity<ResponseKakaoApi> response = kakaoMapApi.getSearchPlaceByKeyword(
            requestKakao);
        return ResponseEntity.ok().body(response.getBody());

    }
}
