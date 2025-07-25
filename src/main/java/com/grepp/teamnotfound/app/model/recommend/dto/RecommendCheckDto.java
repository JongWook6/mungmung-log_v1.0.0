package com.grepp.teamnotfound.app.model.recommend.dto;

import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendCheckDto {

    private LifeRecordListDto listDto;
    private LifeRecordAvgDto avgDto;
    private RecommendStateDto stateDto;
    private PetInfoDto petInfoDto;
    private Standard standard;

}
