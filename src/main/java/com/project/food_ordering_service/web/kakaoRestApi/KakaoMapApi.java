package com.project.food_ordering_service.web.kakaoRestApi;

import com.project.food_ordering_service.web.kakaoRestApi.dto.RequsetKakao;
import com.project.food_ordering_service.web.kakaoRestApi.dto.ResponseKakaoApi;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMapApi {

    private final RestClient restClient;

    private String kakaoSecretKey = "63dba8998396306fc660d71333300d28";

    public static final String kakaoHeader = "KakaoAK ";

    public static final String kakaoHost = "https://dapi.kakao.com";

    public static final String kakaoURL = "/v2/local/search/category.json";

    public ResponseKakaoApi getSearchPlaceByKeyword(RequsetKakao requsetKakao) {

        URI url = UriComponentsBuilder.fromHttpUrl(kakaoHost + kakaoURL)
            .queryParam("category_group_code", "CE7")
            .queryParam("x", requsetKakao.getX())
            .queryParam("y", requsetKakao.getY())
            .queryParam("radius", 1000) // 1km
            .build()
            .toUri();

        return restClient.get()
            .uri(url)
            .header(kakaoHeader + kakaoSecretKey)
            .retrieve()
            .body(ResponseKakaoApi.class);
    }
}
