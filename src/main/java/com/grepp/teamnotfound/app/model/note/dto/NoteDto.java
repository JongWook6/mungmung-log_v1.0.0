package com.grepp.teamnotfound.app.model.note.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {

    private Long noteId;

    @NotNull
    private String content;

    @NotNull
    private Long pet;
}
