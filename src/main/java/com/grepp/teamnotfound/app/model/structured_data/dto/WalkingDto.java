package com.grepp.teamnotfound.app.model.structured_data.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WalkingDto {

    private Long walkingId;

    @NotNull
    private OffsetDateTime startedAt;

    @NotNull
    private OffsetDateTime endedAt;

    @NotNull
    private Integer pace;

    @NotNull
    private LocalDate recordedAt;

    @NotNull
    private Long pet;

}
