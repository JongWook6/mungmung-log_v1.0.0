package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.teamnotfound.app.model.reply.entity.Reply;
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
    @JsonProperty("isFirst") // JSON 필드명을 isFirst 로 지정
    private boolean isFirst;
    @JsonProperty("isLast") // JSON 필드명을 isLast 로 지정
    private boolean isLast;

    public static PageInfo fromPage(Page<?> replyPage) {
        return PageInfo.builder()
            .currentPage(replyPage.getNumber() + 1)
            .totalPages(replyPage.getTotalPages())
            .totalElements(replyPage.getTotalElements())
            .isFirst(replyPage.hasPrevious())
            .isLast(replyPage.hasNext())
            .build();
    }
}
