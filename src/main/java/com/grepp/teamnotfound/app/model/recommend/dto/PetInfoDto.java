package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.pet.code.PetPhase;
import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetInfoDto {

    private PetType breed;
    private PetSize size;
    private PetPhase age;

    public static PetInfoDto toDto(Standard standard){
        return PetInfoDto.builder()
                .breed(standard.getBreed())
                .size(standard.getSize())
                .age(standard.getAge())
                .build();
    }

}
