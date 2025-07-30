package com.grepp.teamnotfound.app.model.recommend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiFullResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendCheckDto;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiRequestDto;
import com.grepp.teamnotfound.infra.error.exception.GeminiException;
import com.grepp.teamnotfound.infra.error.exception.code.GeminiErrorCode;

import java.time.LocalDate;
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
            너는 반려동물의 건강 데이터를 분석하여 맞춤형 조언을 해주는 AI 헬스케어 전문가야.
            주어진 '반려견의 정보'에 포함된 건강 상태(state)와 '최근 10일 기록'을 분석해서, 가장 중요한 문제 하나에 대한 핵심 조언을 생성해 줘.
            
            # 반려견의 정보 (Pet's Information)
            # 'VERY_LOW', 'LOW', 'NORMAL', "HIGH", "VERY_HIGH" 등의 문자열 상태값을 전달합니다.
            {
              "weightState": "%s", "sleepingState": "%s", "walkingState": "%s",
              "breed": "%s", "age": %s, "size": "%s"
            }
            
            # 반려견의 최근 10일 기록 (Pet's Recent Data)
            {
              "weightList": [%s], "avgWeight": %.2f,
              "sleepTimeList": [%s], "avgSleepTime": %.2f,
              "walkingList": [%s], "avgWalking": %.2f
            }
            
            # 수행 작업 (Task to Perform)
            1. '반려견의 정보'에 있는 세 가지 상태(weightState, sleepingState, walkingState)를 확인해.
            2. 'NORMAL'이 아닌 상태 중, 가장 우려되는 항목을 하나만 선택해.
            3. 만약 모든 상태가 '정상'이라면, "지금처럼 건강하게 잘 관리해주세요!"와 같이 긍정적인 메시지를 생성해.
            4. 문제가 되는 항목이 선택되면, 해당 항목의 '최근 10일 기록' 리스트를 보고 추세(지속적인 증가/감소 등)를 파악해.
            5. 진단된 상태와 데이터 추세를 종합해서, 보호자가 즉시 실천할 수 있는 구체적인 조언을 '출력 형식'에 맞춰 생성해.
            
            # 출력 형식 (Output Format)
            {
              "recommendation": "여기에 반려견 정보를 제외한 맞춤형 제안 문구 공백 포함 60자 이하"
            }
            """,
            // 반려견 정보
            checkDto.getStateDto().getWeightState(), checkDto.getStateDto().getSleepingState(), checkDto.getStateDto().getWalkingState(),
            checkDto.getPetInfoDto().getBreed(), checkDto.getPetInfoDto().getAge(), checkDto.getPetInfoDto().getSize(),

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

    // Gemini 응답 생성
    public String generateAnalysis(List<String> notes) {
        // 프롬프트 생성
        String prompt = createAnalysisPrompt(notes);
        // Gemini 응답 생성
        String geminiApiResponse = getGeminiResponse(prompt);
        System.out.println(geminiApiResponse);
        // 응답 데이터로 변경
        return parseGeminiAnalysisResponse(geminiApiResponse);
    }

    private String createAnalysisPrompt(List<String> notes) {
        return String.format("""
            다음은 일주일 동안 보호자가 나(강아지)를 관찰하며 남긴 기록이야.
            이 기록을 바탕으로, 내가 어떤 기분인지 보호자에게 한두 문장으로 이야기해줘.
            
            나는 강아지고, 보호자에게 말하듯 귀엽고 따뜻하게 말할게.
            말투는 자연스럽고 친근하게 해줘. 너무 길거나 딱딱하지 않게.
            
            예시 : 요즘 너무 신나! 산책도 즐겁고 간식도 최고야!
            
            # 강아지의 최근 관찰노트 (Pet's Recent Data)
            {
              "notes": [%s]
            }
            
            """, notes.toString()
        );
    }

    public String parseGeminiAnalysisResponse(String geminiApiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // FullResponse DTO 파싱
            GeminiFullResponse fullDto = mapper.readValue(geminiApiResponse, GeminiFullResponse.class);

            // 데이터 추출
            return Optional.ofNullable(fullDto.getCandidates())
                    .filter(candidates -> !candidates.isEmpty())
                    .map(List::getFirst)
                    .map(GeminiFullResponse.Candidate::getContent)
                    .map(GeminiFullResponse.Content::getParts)
                    .filter(parts -> !parts.isEmpty())
                    .map(List::getFirst)
                    .map(GeminiFullResponse.Part::getText)
                    .map(text -> text.replace("```json", "").replace("```", "").trim())
                    .orElseThrow(() -> new GeminiException(GeminiErrorCode.GEMINI_REQUIRED_CONTENT));

        } catch (Exception e) {
            throw new GeminiException(GeminiErrorCode.GEMINI_PARSING_ERROR);
        }
    }
}