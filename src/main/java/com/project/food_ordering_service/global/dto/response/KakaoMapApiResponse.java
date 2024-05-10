package com.project.food_ordering_service.global.dto.response;

import com.project.food_ordering_service.global.dto.response.Documents;
import com.project.food_ordering_service.global.dto.response.Meta;
import java.util.List;
import lombok.Getter;

@Getter
public class KakaoMapApiResponse {

    private Meta meta;

    private List<Documents> document;


}
