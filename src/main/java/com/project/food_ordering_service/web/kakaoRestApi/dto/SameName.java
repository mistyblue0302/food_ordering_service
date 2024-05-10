package com.project.food_ordering_service.web.kakaoRestApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SameName {

    @JsonProperty("region")
    private String[] region;

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("selected_region")
    private String selectedRegion;

}
