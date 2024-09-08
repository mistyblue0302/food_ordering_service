package com.project.food_ordering_service.global.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meta {

    @JsonProperty("total_count")
    private final Integer totalCount;

    @JsonProperty("pageable_count")
    private final Integer pageableCount;

    @JsonProperty("is_end")
    private final Boolean isEnd;

    @JsonProperty("same_name")
    private SameName sameName;

    @JsonCreator
    public Meta(
            @JsonProperty("total_count") Integer totalCount,
            @JsonProperty("pageable_count") Integer pageableCount,
            @JsonProperty("is_end") Boolean isEnd,
            @JsonProperty("same_name") SameName sameName) {
        this.totalCount = totalCount;
        this.pageableCount = pageableCount;
        this.isEnd = isEnd;
        this.sameName = sameName;
    }
}