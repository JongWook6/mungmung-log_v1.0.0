package com.grepp.teamnotfound.app.controller.api.reply.payload;

import com.grepp.teamnotfound.app.model.reply.dto.ReplyListDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ReplyListResponse {
    private List<ReplyListDto> data = new ArrayList<>();
}
