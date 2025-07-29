package com.grepp.teamnotfound.app.controller.api.liferecord.payload;

import com.grepp.teamnotfound.app.model.liferecord.dto.LifeRecordListDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LifeRecordListResponse {

    private List<LifeRecordListDto> data;
    private PageInfo pageInfo;

}
