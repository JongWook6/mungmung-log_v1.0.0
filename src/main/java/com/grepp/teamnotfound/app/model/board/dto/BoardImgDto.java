package com.grepp.teamnotfound.app.model.board.dto;

import com.grepp.teamnotfound.infra.code.ImgType;
import lombok.Data;

@Data
public class BoardImgDto {

    private Long boardImgId;

    private Long boardId;

    private String savePath;

    private ImgType imgType;

}
