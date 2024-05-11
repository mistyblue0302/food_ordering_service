package com.project.food_ordering_service.global.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class KakaoMapApiResponse {

    private Meta meta;

    private List<Documents> document;


}
