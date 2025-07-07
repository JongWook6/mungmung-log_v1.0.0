package com.grepp.teamnotfound.app.model.vaccination.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.teamnotfound.app.model.vaccination.code.VaccineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class VaccinationDTO {

    private Long vaccinationId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
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
