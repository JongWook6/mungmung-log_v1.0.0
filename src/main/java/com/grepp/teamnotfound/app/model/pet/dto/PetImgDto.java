package com.grepp.teamnotfound.app.model.pet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PetImgDTO {

    private Long petImgId;

    @NotNull
    @Size(max = 255)
    private String savePath;

    @NotNull
    @Size(max = 20)
    private String type;

    @NotNull
    @Size(max = 255)
    private String originName;

    @NotNull
    @Size(max = 255)
    private String renamedName;

    @NotNull
    private Long pet;

}
