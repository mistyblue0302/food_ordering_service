package com.project.food_ordering_service.web.kakaoRestApi.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class RequestKakaoApi {

    private Meta meta;

    private List<Documents> document;


}
