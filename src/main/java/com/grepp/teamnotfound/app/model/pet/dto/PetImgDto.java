package com.grepp.teamnotfound.app.model.pet.dto;

import com.grepp.teamnotfound.infra.code.ImgType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetImgDTO {

    private Long petImgId;
    private String savePath;
    private ImgType type;
    private String originName;
    private String renamedName;

}
