package com.grepp.teamnotfound.app.model.pet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PetDTO {

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
    private PetType breed;

    @NotNull
    private PetSize size;

    private Double weight;

    @NotNull
    private Boolean sex;

    @NotNull
    @JsonProperty("isNeutered")
    private Boolean isNeutered;

    private Long user;

}
