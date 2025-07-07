package com.grepp.teamnotfound.app.model.board.dto;

import com.grepp.teamnotfound.infra.code.ImgType;
import lombok.Data;

@Data
public class ArticleImgDto {

    private Long articleImgId;

    private Long articledId;

    private String savePath;

    private ImgType imgType;

}
