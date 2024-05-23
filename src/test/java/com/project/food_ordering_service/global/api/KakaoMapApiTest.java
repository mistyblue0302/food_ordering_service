package com.project.food_ordering_service.global.api;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.food_ordering_service.global.dto.request.KakaoMapApiRequest;
import com.project.food_ordering_service.global.dto.response.Documents;
import com.project.food_ordering_service.global.dto.response.KakaoMapApiResponse;
import com.project.food_ordering_service.global.dto.response.Meta;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

@RestClientTest(KakaoMapApi.class)
class KakaoMapApiTest {

    @Autowired
    KakaoMapApi kakaoMapApi;

    @Autowired
    MockRestServiceServer server;

    @Autowired
    ObjectMapper objectMapper;

    private static final String JSON_RESPONSE = """
        {
            "meta": {
                "total_count": 5014,
                "pageable_count": 45,
                "is_end": false,
                "same_name": null
            },
            "documents": [
                {
                    "id": "1065693087",
                    "place_name": "금돼지식당",
                    "category_name": "음식점 > 한식 > 육류,고기",
                    "category_group_code": "FD6",
                    "category_group_name": "음식점",
                    "phone": "0507-1307-8750",
                    "address_name": "서울 중구 신당동 370-69",
                    "road_address_name": "서울 중구 다산로 149",
                    "x": 127.01167974212188,
                    "y": 37.55705875134064,
                    "place_url": "http://place.map.kakao.com/1065693087",
                    "distance": "0"
                },
                {
                    "id": "21722308",
                    "place_name": "만포막국수",
                    "category_name": "음식점 > 한식 > 국수",
                    "category_group_code": "FD6",
                    "category_group_name": "음식점",
                    "phone": "02-2235-1357",
                    "address_name": "서울 중구 신당동 425-18",
                    "road_address_name": "서울 중구 동호로14길 2",
                    "x": 127.01031863376261,
                    "y": 37.555830097762694,
                    "place_url": "http://place.map.kakao.com/21722308",
                    "distance": "181"
                }
            ]
        }
        """;


    @Test
    @DisplayName("확인중")
    void getSearchPlaceByKeyword() throws JsonProcessingException {
        //given
        Meta meta = Meta.builder()
            .totalCount(5014)
            .pageableCount(45)
            .isEnd(false)
            .sameName(null)
            .build();
        List<Documents> documentsList = new ArrayList<>();

        // Document 1
        Documents document1 = Documents.builder()
            .id("1065693087")
            .placeName("금돼지식당")
            .categoryName("음식점 > 한식 > 육류,고기")
            .categoryGroupCode("FD6")
            .categoryGroupName("음식점")
            .phone("0507-1307-8750")
            .addressName("서울 중구 신당동 370-69")
            .roadAddressName("서울 중구 다산로 149")
            .x(127.01167974212188)
            .y(37.55705875134064)
            .placeUrl("http://place.map.kakao.com/1065693087")
            .distance("0")
            .build();

        // Add document to list
        documentsList.add(document1);

        // Document 2
        Documents document2 = Documents.builder()
            .id("21722308")
            .placeName("만포막국수")
            .categoryName("음식점 > 한식 > 국수")
            .categoryGroupCode("FD6")
            .categoryGroupName("음식점")
            .phone("02-2235-1357")
            .addressName("서울 중구 신당동 425-18")
            .roadAddressName("서울 중구 동호로14길 2")
            .x(127.01031863376261)
            .y(37.555830097762694)
            .placeUrl("http://place.map.kakao.com/21722308")
            .distance("181")
            .build();

        KakaoMapApiResponse data = new KakaoMapApiResponse(meta, documentsList);

        KakaoMapApiRequest request = new KakaoMapApiRequest(127.01167974212188, 37.55705875134064);

        final String kakaoHost = "https://dapi.kakao.com";

        final String kakaoURL = "/v2/local/search/category.json";

        URI url = UriComponentsBuilder.fromHttpUrl(kakaoHost + kakaoURL)
            .queryParam("category_group_code", "FD6")
            .queryParam("x", request.getX())
            .queryParam("y", request.getY())
            .queryParam("radius", 2000) // 2km
            .build()
            .toUri();

        //when
        this.server
            .expect(requestTo(url))
            .andRespond(
                withSuccess(objectMapper.writeValueAsString(data), MediaType.APPLICATION_JSON));

        //then
        ResponseEntity<KakaoMapApiResponse> result = kakaoMapApi.getSearchPlaceByKeyword(request);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(5014, result.getBody().getMeta().getTotalCount());
    }
}