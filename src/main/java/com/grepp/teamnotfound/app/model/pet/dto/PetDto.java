package com.grepp.teamnotfound.app.model.pet.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PetDto {

    private Long petId;

    @NotNull
    @Size(max = 50)
    private String registNumber;

    @NotNull
    private LocalDate birthday;

    @NotNull
    private LocalDate metday;

    @Size(max = 10)
    private String name;

    @NotNull
    private Integer age;

    @NotNull
    @Size(max = 50)
    private String breed;

    @NotNull
    @Size(max = 10)
    private String size;

    private Double weight;

    @NotNull
    private Boolean sex;

    @NotNull
    @JsonProperty("isNeutered")
    private Boolean isNeutered;

    private Long user;

}
