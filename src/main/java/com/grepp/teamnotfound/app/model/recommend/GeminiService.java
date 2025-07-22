package com.grepp.teamnotfound.app.model.recommend;

import com.grepp.teamnotfound.app.model.recommend.dto.GeminiRequestDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public GeminiService(@Qualifier("geminiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public String getGeminiResponse(String prompt) {
        GeminiRequestDto request = new GeminiRequestDto(prompt);

        return webClient.post()
                .uri("/models/gemini-2.0-flash-lite:generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}