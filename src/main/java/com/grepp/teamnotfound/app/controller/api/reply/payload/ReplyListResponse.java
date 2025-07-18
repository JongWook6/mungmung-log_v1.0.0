package com.grepp.teamnotfound.app.controller.api.reply.payload;

import com.grepp.teamnotfound.app.controller.api.article.payload.PageInfo;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyListResponse {
    private List<ReplyDetailResponse> replyList;
    private PageInfo pageInfo;
}
