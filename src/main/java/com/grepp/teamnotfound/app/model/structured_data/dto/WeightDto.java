package com.grepp.teamnotfound.app.model.structured_data.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeightDto {

    private Long weightId;

    @NotNull
    private Double weight;

    @NotNull
    private LocalDate recordedAt;

    @NotNull
    private Long pet;

}
