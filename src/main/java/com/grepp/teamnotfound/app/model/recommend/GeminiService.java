package com.grepp.teamnotfound.app.model.recommend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.teamnotfound.app.controller.api.recommend.payload.GeminiFullResponse;
import com.grepp.teamnotfound.app.controller.api.recommend.payload.GeminiResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendRequestDto;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiRequestDto;
import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import com.grepp.teamnotfound.infra.error.exception.GeminiException;
import com.grepp.teamnotfound.infra.error.exception.code.GeminiErrorCode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    // 프롬프트 생성
    public String createPrompt(RecommendRequestDto request, Standard standard){
        String weightListStr = request.getWeightList().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String sleepTimeListStr = request.getSleepTimeList().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String walkingListStr = request.getWalkingList().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        // 전체 프롬프트를 구성
        return String.format("""
            너는 반려동물의 건강 데이터를 분석하여 상태를 진단하는 AI 전문가다.
            주어진 '건강 가이드라인'과 '반려견의 최근 10일 기록'을 비교 분석해라.
            각 항목(몸무게, 수면, 식사, 산책)에 대해 현재 상태를 5단계('매우 부족', '부족', '적정', '과다', '매우 과다') 중 하나로 분류해야 한다.

            분석 기준은 다음과 같다:
            1. 몸무게: '최근 10일 기록'의 가장 최신 몸무게(weightList의 첫 번째 값)를 '건강 가이드라인'의 minWeight, maxWeight와 비교하여 판단한다.
            2. 수면/식사/산책: '최근 10일 기록'의 평균값(avgSleepTime 등)을 '건강 가이드라인'의 최소, 최대 권장량과 비교하여 판단한다. 일별 데이터(List)의 변동성도 참고하여 너무 불규칙할 경우 '부족' 또는 '과다' 쪽으로 판단할 수 있다.

            **절대로 다른 설명이나 문장을 추가하지 말고, 아래에 명시된 JSON 형식만 응답해야 한다.**

            # 건강 가이드라인 (Standard Data)
            {
              "minWeight": %.1f, "maxWeight": %.1f,
              "minSleep": %d, "maxSleep": %d,
              "minWalk": %d, "maxWalk": %d
            }

            # 반려견의 최근 10일 기록 (Pet's Recent Data)
            {
              "weightList": [%s], "avgWeight": %.2f,
              "sleepTimeList": [%s], "avgSleepTime": %.2f,
              "walkingList": [%s], "avgWalking": %.2f
            }

            # 출력 형식 (Output Format)
            {
              "weight": {
                "status": "여기에 분류 결과",
                "recommendation": "여기에 몸무게 관련 맞춤형 제안 문구"
              },
              "sleep": {
                "status": "여기에 분류 결과",
                "recommendation": "여기에 수면 관련 맞춤형 제안 문구"
              },
              "walk": {
                "status": "여기에 분류 결과",
                "recommendation": "여기에 산책 관련 맞춤형 제안 문구"
              }
            }
            """,
            // 건강 가이드라인 데이터
            standard.getMinWeight(), standard.getMaxWeight(),
            standard.getMinSleep(), standard.getMaxSleep(),
            standard.getMinWalk(), standard.getMaxWalk(),

            // 반려견 기록 데이터
            weightListStr, request.getAvgWeight(),
            sleepTimeListStr, request.getAvgSleepTime(),
            walkingListStr, request.getAvgWalking()
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