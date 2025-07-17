package com.grepp.teamnotfound.app.model.board.dto;

import com.grepp.teamnotfound.app.model.board.entity.ArticleImg;
import com.grepp.teamnotfound.infra.code.ImgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleImgDto {

    private Long articleImgId;

    private Long articleId;

    private String savePath;

    private ImgType imgType;
}
