package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteResponse {

    private Long noteId;
    private String content;

}
