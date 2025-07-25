package com.grepp.teamnotfound.app.model.recommend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequestDto {

    private List<Content> contents;

    public GeminiRequestDto(String userText) {
        this.contents = List.of(new Content("user", List.of(new Part(userText))));
    }

    @Getter
    @AllArgsConstructor
    static class Content {
        private String role;
        private List<Part> parts;

    }

    @Getter
    @AllArgsConstructor
    static class Part {
        private String text;
    }
}
