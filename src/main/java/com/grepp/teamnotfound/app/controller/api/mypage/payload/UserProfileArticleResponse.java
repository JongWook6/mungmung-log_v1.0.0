package com.grepp.teamnotfound.app.controller.api.mypage.payload;

import com.grepp.teamnotfound.app.controller.api.article.payload.PageInfo;
import com.grepp.teamnotfound.app.model.board.dto.UserArticleListDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileArticleResponse {
    private List<UserArticleListDto> articles;
    private PageInfo pageInfo;
}
