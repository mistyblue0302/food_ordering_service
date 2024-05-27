package com.project.food_ordering_service.global.api;

import com.project.food_ordering_service.global.dto.request.KakaoMapApiRequest;
import com.project.food_ordering_service.global.dto.response.KakaoMapApiResponse;
import java.net.URI;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class KakaoMapApi {

    private final RestClient restClient;

    public KakaoMapApi(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("")
            .build();
    }

    @Value("${KAKAO_SECRET}")
    private String kakaoSecretKey;

    public static final String kakaoHeader = "KakaoAK ";

    public static final String kakaoHost = "https://dapi.kakao.com";

    public static final String kakaoURL = "/v2/local/search/category.json";

    public ResponseEntity<KakaoMapApiResponse> getSearchPlaceByKeyword(
        KakaoMapApiRequest kakaoMapApiRequest) {

        URI url = UriComponentsBuilder.fromHttpUrl(kakaoHost + kakaoURL)
            .queryParam("category_group_code", "FD6")
            .queryParam("x", kakaoMapApiRequest.getX())
            .queryParam("y", kakaoMapApiRequest.getY())
            .queryParam("radius", 2000) // 2km
            .build()
            .toUri();

        log.info(url.getQuery() + " url");
        return restClient.get()
            .uri(url)
            .header("Authorization", kakaoHeader + kakaoSecretKey)
            .retrieve()
            .toEntity(KakaoMapApiResponse.class);

    }
}
