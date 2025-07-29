package com.grepp.teamnotfound.app.controller.api.liferecord.payload;

import com.grepp.teamnotfound.app.model.liferecord.dto.LifeRecordListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {
    private int page;
    private int size;
    private int totalPages;
    private int totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

    public static PageInfo of(Page<LifeRecordListDto> page) {
        return PageInfo.builder()
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalPages(page.getTotalPages())
            .totalElements((int) page.getTotalElements())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
    }

}
