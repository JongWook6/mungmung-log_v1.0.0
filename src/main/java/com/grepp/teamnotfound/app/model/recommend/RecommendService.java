package com.grepp.teamnotfound.app.model.recommend;

import com.grepp.teamnotfound.app.controller.api.recommend.payload.GeminiResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendRequestDto;
import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.life_record.repository.LifeRecordRepository;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import com.grepp.teamnotfound.app.model.recommend.repository.RecommendRepository;
import com.grepp.teamnotfound.app.model.recommend.repository.StandardRepository;
import com.grepp.teamnotfound.infra.error.exception.StandardException;
import com.grepp.teamnotfound.infra.error.exception.code.StandardErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final LifeRecordRepository lifeRecordRepository;
    private final StandardRepository standardRepository;
    private final GeminiService geminiService;

    public Recommend matchDailyRecommend(Pet pet, LocalDate date) {
        return null;
    }

    // Recommend 생성
    @Transactional(readOnly = true)
    public GeminiResponse getRecommend(Pet pet) {
        List<LifeRecord> lifeRecordList = lifeRecordRepository.findTop10ByPet(pet);

        // 질문에 필요한 데이터 만들기
        RecommendRequestDto dto = RecommendRequestDto.toDto(pet, lifeRecordList);

        // 반려견의 기준표 반환
        Standard standard = standardRepository.findStandardByBreedAndSizeAndAge(dto.getBreed(), dto.getSize(), dto.getAge())
                .orElseThrow(() -> new StandardException(StandardErrorCode.STANDARD_NOT_FOUND));

        // 프롬프트 생성
        String prompt = geminiService.createPrompt(dto, standard);
        // Gemini 응답 생성
        String geminiApiResponse = geminiService.getGeminiResponse(prompt);
        // 응답 데이터로 변경
        GeminiResponse response = geminiService.parseGeminiResponse(geminiApiResponse);

        return response;
    }

}
