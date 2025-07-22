package com.grepp.teamnotfound.app.model.life_record.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NoteDto {
    private LocalDate date;
    private String content;
}
