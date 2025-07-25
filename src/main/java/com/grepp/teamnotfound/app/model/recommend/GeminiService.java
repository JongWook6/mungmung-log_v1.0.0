package com.grepp.teamnotfound.app.model.recommend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiFullResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendCheckDto;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiRequestDto;
import com.grepp.teamnotfound.infra.error.exception.GeminiException;
import com.grepp.teamnotfound.infra.error.exception.code.GeminiErrorCode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public GeminiService(@Qualifier("geminiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    // Gemini 응답 생성
    public GeminiResponse getGemini(RecommendCheckDto checkDto) {
        // 프롬프트 생성
        String prompt = createPrompt(checkDto);
        // Gemini 응답 생성
        String geminiApiResponse = getGeminiResponse(prompt);
        // 응답 데이터로 변경
        return parseGeminiResponse(geminiApiResponse);
    }

    // 프롬프트 생성
    public String createPrompt(RecommendCheckDto checkDto){
        String weightListStr = checkDto.getListDto().getWeightList().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String sleepTimeListStr = checkDto.getListDto().getSleepTimeList().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String walkingListStr = checkDto.getListDto().getWalkingList().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        // 전체 프롬프트를 구성
        return String.format("""
            너는 반려동물의 건강 데이터를 보고 맞춤형 제안을 해주는 AI 전문가야.
            주어진 '건강 가이드라인'과 '반려견의 최근 10일 몸무게, 수면시간, 산책량 기록 및 평균'과 '반려견의 최근 10일 몸무게, 수면시간, 산책량 상태'을 보고 자세한 맞춤형 제안을 해줘.
            
            # 반려견의 최근 10일 기록 (Pet's Recent Data)
            {
              "weightList": [%s], "avgWeight": %.2f,
              "sleepTimeList": [%s], "avgSleepTime": %.2f,
              "walkingList": [%s], "avgWalking": %.2f
            }
            
            # 출력 형식 (Output Format)
            {
              "recommendation": "여기에 반려견 정보를 제외한 맞춤형 제안 문구 3줄 이하"
            }
            """,
            // 반려견 기록 데이터
            weightListStr, checkDto.getAvgDto().getAvgWeight(),
            sleepTimeListStr, checkDto.getAvgDto().getAvgSleepTime(),
            walkingListStr, checkDto.getAvgDto().getAvgWalking()
        );
    }

    // Gemini 응답 생성
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

    // 응답 데이터로 변경
    public GeminiResponse parseGeminiResponse(String geminiApiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // FullResponse DTO 파싱
            GeminiFullResponse fullDto = mapper.readValue(geminiApiResponse, GeminiFullResponse.class);

            // 데이터 추출
            String cleanedJson = Optional.ofNullable(fullDto.getCandidates())
                    .filter(candidates -> !candidates.isEmpty())
                    .map(List::getFirst)
                    .map(GeminiFullResponse.Candidate::getContent)
                    .map(GeminiFullResponse.Content::getParts)
                    .filter(parts -> !parts.isEmpty())
                    .map(List::getFirst)
                    .map(GeminiFullResponse.Part::getText)
                    .map(text -> text.replace("```json", "").replace("```", "").trim())
                    .orElseThrow(() -> new GeminiException(GeminiErrorCode.GEMINI_REQUIRED_CONTENT));

            // String 데이터 GeminiResponse로 Parsing
            return mapper.readValue(cleanedJson, GeminiResponse.class);
        } catch (Exception e) {
            throw new GeminiException(GeminiErrorCode.GEMINI_PARSING_ERROR);
        }
    }

}