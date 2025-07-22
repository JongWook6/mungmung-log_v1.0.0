package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
