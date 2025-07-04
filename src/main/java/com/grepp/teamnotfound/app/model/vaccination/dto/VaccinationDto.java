package com.grepp.teamnotfound.app.model.vaccination.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VaccinationDto {

    private Long vaccinationId;

    @NotNull
    private OffsetDateTime vaccineAt;

    @NotNull
    @Size(max = 20)
    private String vaccineType;

    @NotNull
    private Integer count;

    @NotNull
    @JsonProperty("isVaccine")
    private Boolean isVaccine;

    @NotNull
    private Long vaccine;

    @NotNull
    private Long pet;

}
