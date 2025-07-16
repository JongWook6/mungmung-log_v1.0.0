package com.grepp.teamnotfound.app.controller.api.article.payload;

import com.grepp.teamnotfound.app.model.board.code.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 30, message = "제목은 1자 이상 30자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 2000, message = "내용은 1자 이상 2000자 이하로 입력해주세요.")
    private String content;

    @NotNull(message = "게시판 타입은 필수입니다.")
    private BoardType boardType;
}
