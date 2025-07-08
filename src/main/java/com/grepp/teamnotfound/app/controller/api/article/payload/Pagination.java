package com.grepp.teamnotfound.app.controller.api.article.payload;

import lombok.Builder;
import lombok.Data;

@Data
public class Pagination {
    private int total;
    private int page;
    private int size;
    private int totalPages;
}
