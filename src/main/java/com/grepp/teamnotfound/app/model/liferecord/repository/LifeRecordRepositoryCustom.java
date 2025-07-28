package com.grepp.teamnotfound.app.model.liferecord.repository;

import com.grepp.teamnotfound.app.controller.api.liferecord.payload.LifeRecordListRequest;
import com.grepp.teamnotfound.app.model.liferecord.dto.LifeRecordListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LifeRecordRepositoryCustom {
    Page<LifeRecordListDto> search(Long userId, LifeRecordListRequest request, Pageable pageable);
}
