package com.project.food_ordering_service.domain.restaurant.controller;

import com.project.food_ordering_service.global.api.KakaoMapApi;
import com.project.food_ordering_service.global.dto.request.KakaoMapApiRequest;
import com.project.food_ordering_service.global.dto.response.KakaoMapApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 * @SpringBootTest : SpringBoot 통합테스트에 사용되는 애노테이션으로 @SpringBootApplication을 찾아가 하위의 모든 빈을 스캔한다.
 * @ActiveProfiles : 스프링 컨테이터 실행 시 실행 환경을 지정해주는 애노테이션으로, 테스트 시 특정 빈을 로드
 */
@SpringBootTest
@ActiveProfiles("test")
class RestaurantControllerTest {

    @Autowired
    KakaoMapApi kakaoMapApi;

    @Test
    @DisplayName("카카오 api 불러오기")
    void getAddressByCoordinates() {

        int x = (int) 127.02179955803058;
        int y = (int) 37.57134801872825;

        KakaoMapApiRequest request = new KakaoMapApiRequest(x, y);

        // given
        ResponseEntity<KakaoMapApiResponse> response = kakaoMapApi.getSearchPlaceByKeyword(request);

        // when
        KakaoMapApiResponse result = response.getBody();

        // then
        Assertions.assertNotNull(result);
    }

}