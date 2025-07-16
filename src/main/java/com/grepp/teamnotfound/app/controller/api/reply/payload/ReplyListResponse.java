package com.grepp.teamnotfound.app.controller.api.reply.payload;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyListResponse {
    private List<ReplyDetailResponse> data;
    private int currentPage;
    private int totalPage;
    private long totalElements;
    private boolean isFirst;
    private boolean isLast;
}
