package com.grepp.teamnotfound.app.controller.api.admin.payload;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    private int currentPage;
    private int totalPages;
    private long totalElements;
    @JsonProperty("isFirst")
    private boolean isFirst;
    @JsonProperty("isLast")
    private boolean isLast;

    public static PageInfo fromPage(Page<?> replyPage) {
        return PageInfo.builder()
                .currentPage(replyPage.getNumber() + 1)
                .totalPages(replyPage.getTotalPages())
                .totalElements(replyPage.getTotalElements())
                .isFirst(replyPage.isFirst())
                .isLast(replyPage.isLast())
                .build();
    }
}
