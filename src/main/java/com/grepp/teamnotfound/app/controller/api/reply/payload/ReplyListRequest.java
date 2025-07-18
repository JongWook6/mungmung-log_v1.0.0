package com.grepp.teamnotfound.app.controller.api.reply.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReplyListRequest {

    @Min(value = 1, message = "페이지는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "페이지 값이 너무 큽니다.")
    private int page = 1;

    @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "사이즈 값이 너무 큽니다.")
    private int size = 10;
}
