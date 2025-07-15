package com.grepp.teamnotfound.app.model.board.dto;

import com.grepp.teamnotfound.infra.code.ImgType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleImgDto {

    private Long articleImgId;

    private String savePath;

    private ImgType imgType;

}
