package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleRequest {
    // TODO Validation 필요

    private Long userId;
    private String title;
    private String content;
    private BoardType boardType;
}
