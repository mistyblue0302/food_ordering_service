package com.project.food_ordering_service.domain.restaurant.controller;

import com.project.food_ordering_service.domain.restaurant.service.RestaurantService;
import com.project.food_ordering_service.global.dto.request.KakaoMapApiRequest;
import com.project.food_ordering_service.global.dto.response.KakaoMapApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "레스토랑", description = "Restaurant API")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "주변음식점 불러오기", description = "카카오 주변 음식점 불러오기")
    @Parameters({
        @Parameter(name = "x", description = "위도", example = "127.01167974212188"),
        @Parameter(name = "y", description = "경도", example = "37.55705875134064"),
    })
    @PostMapping("/restaurant")
    public ResponseEntity<KakaoMapApiResponse> getRestaurant(
        @RequestBody KakaoMapApiRequest kakaoMapApiRequest) {
        ResponseEntity<KakaoMapApiResponse> response = restaurantService.getRestaurant(
            kakaoMapApiRequest);
        return ResponseEntity.ok().body(response.getBody());
    }
}
