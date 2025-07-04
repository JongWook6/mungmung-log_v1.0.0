package com.grepp.teamnotfound.app.model.board.dto;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BoardDto {

    private Long boardId;

    private Long userId;

    private String title;

    private String content;

    private BoardType boardType;

    private Integer views;

    private OffsetDateTime reportedAt;

    private OffsetDateTime createdAt;

    private List<BoardImgDto> images;

}
