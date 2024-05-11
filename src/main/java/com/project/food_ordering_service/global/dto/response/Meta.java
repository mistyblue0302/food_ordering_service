package com.project.food_ordering_service.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Meta {

    @JsonProperty("total_count")
    private final Integer totalCount;

    @JsonProperty("pageable_count")
    private final Integer pageableCount;

    @JsonProperty("is_end")
    private final Boolean isEnd;

    @JsonProperty("same_name")
    private SameName sameName;


}
