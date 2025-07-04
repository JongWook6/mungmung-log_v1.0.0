package com.grepp.teamnotfound.app.model.recommend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RecommendDto {

    private Long recId;

    @NotNull
    @Size(max = 6)
    private String age;

    @NotNull
    @Size(max = 20)
    private String breed;

    @NotNull
    @Size(max = 20)
    private String size;

    @NotNull
    @Size(max = 20)
    private String weight;

    @NotNull
    @Size(max = 20)
    private String walking;

    @NotNull
    @Size(max = 20)
    private String sleeping;

    @NotNull
    @Size(max = 60)
    private String content;

}
