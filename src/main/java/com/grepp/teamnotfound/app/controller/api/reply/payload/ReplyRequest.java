package com.grepp.teamnotfound.app.controller.api.reply.payload;

import lombok.Data;

@Data
public class ReplyRequest {
    // TODO Validation 필요

    private Long userId;
    private String content;
}
