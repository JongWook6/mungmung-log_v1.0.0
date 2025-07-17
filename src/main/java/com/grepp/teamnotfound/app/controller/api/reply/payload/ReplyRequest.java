package com.grepp.teamnotfound.app.controller.api.reply.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReplyRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 255, message = "내용은 1자 이상 255자 이하로 입력해주세요.")
    private String content;
}
