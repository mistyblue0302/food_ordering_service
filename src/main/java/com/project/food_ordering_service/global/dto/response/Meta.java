package com.project.food_ordering_service.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("pageable_count")
    private Integer pageableCount;

    @JsonProperty("is_end")
    private Boolean isEnd;

    @JsonProperty("same_name")
    private SameName sameName;
}