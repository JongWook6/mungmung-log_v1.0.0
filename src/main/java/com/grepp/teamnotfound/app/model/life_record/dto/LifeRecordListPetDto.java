package com.grepp.teamnotfound.app.model.life_record.dto;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.entity.PetImg;
import com.grepp.teamnotfound.infra.code.ImgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LifeRecordListPetDto {

    private String name;
    private String url;
    private ImgType type;

    public static LifeRecordListPetDto petInfoDto(Pet pet) {
        return pet.getImages().stream()
                .findFirst()
                .map(petImg -> new LifeRecordListPetDto(
                        pet.getName(),
                        petImg.getSavePath() + petImg.getRenamedName(),
                        petImg.getType()
                ))
                .orElse(new LifeRecordListPetDto(pet.getName(), null, null));
    }

}
