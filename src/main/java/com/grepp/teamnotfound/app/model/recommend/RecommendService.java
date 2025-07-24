package com.grepp.teamnotfound.app.model.recommend;

import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import com.grepp.teamnotfound.app.controller.api.recommend.payload.RecommendResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendRequestDto;
import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.life_record.repository.LifeRecordRepository;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import com.grepp.teamnotfound.app.model.recommend.repository.RecommendRepository;
import com.grepp.teamnotfound.app.model.recommend.repository.StandardRepository;
import com.grepp.teamnotfound.infra.error.exception.RecommendException;
import com.grepp.teamnotfound.infra.error.exception.StandardException;
import com.grepp.teamnotfound.infra.error.exception.code.RecommendErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.StandardErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    ModelMapper modelMapper = new ModelMapper();

    public Recommend matchDailyRecommend(Pet pet, LocalDate date) {
        return null;
    }

    @Transactional(readOnly = true)
    public String getRecommend(Pet pet, LocalDate date) {
        Recommend recommend = recommendRepository.findByPetAndDate(pet, date)
                .orElseThrow(() -> new RecommendException(RecommendErrorCode.RECOMMEND_NOT_FOUND));

        return recommend.getContent();
    }

    // Recommend 있는지 확인
    @Transactional(readOnly = true)
    public Boolean existsByPetAndDate(Pet pet) {
        return recommendRepository.existsByPetAndDate(pet, LocalDate.now());
    }

    // 기존 Recommend 가져오기
    @Transactional(readOnly = true)
    public RecommendResponse getRecommendByPet(Pet pet) {
        Recommend recommend = recommendRepository.findByPetAndDate(pet, LocalDate.now())
                .orElseThrow(() -> new RecommendException(RecommendErrorCode.RECOMMEND_NOT_FOUND));

        return RecommendResponse.toResponse(recommend) ;
    }

    // Gemini 응답 생성
    @Transactional(readOnly = true)
    public GeminiResponse getGemini(Pet pet) {
        List<LifeRecord> lifeRecordList = lifeRecordRepository.findTop10ByPet(pet);

        // 질문에 필요한 데이터 만들기
        RecommendRequestDto dto = RecommendRequestDto.toDto(pet, lifeRecordList);

        // 반려견의 기준표 반환
        Standard standard = standardRepository.findStandardByBreedAndAge(dto.getBreed(), dto.getAge())
                .orElseThrow(() -> new StandardException(StandardErrorCode.STANDARD_NOT_FOUND));

        // 프롬프트 생성
        String prompt = geminiService.createPrompt(dto, standard);
        // Gemini 응답 생성
        String geminiApiResponse = geminiService.getGeminiResponse(prompt);
        // 응답 데이터로 변경
        return geminiService.parseGeminiResponse(geminiApiResponse);
    }

    // Recommend 생성
    @Transactional
    public RecommendResponse createRecommend(Pet pet, GeminiResponse response) {
        RecommendResponse res = RecommendResponse.toResponse(response);

        Recommend recommend = modelMapper.map(res, Recommend.class);
        recommend.setPet(pet);

        recommendRepository.save(recommend);

        return res;
    }

    // 기존 Recommend 여부 체크
    @Transactional(readOnly = true)
    public Optional<Recommend> getRecommendByPetStates(Pet pet) {
        Integer age = pet.getAge(pet.getBirthday());
        Standard standard = standardRepository.findStandardByBreedAndAge(pet.getBreed(), age)
                .orElseThrow(() -> new StandardException(StandardErrorCode.STANDARD_NOT_FOUND));

        List<LifeRecord> lifeRecordList = lifeRecordRepository.findTop10ByPet(pet);
        LifeRecordAvgDto avgDto = LifeRecordAvgDto.toDto(lifeRecordList);
        RecommendStateDto stateDto = RecommendStateDto.toDto(avgDto, standard);

        return recommendRepository.findRecommendByAllStates(
                pet.getBreed(),
                standard.getAge(),
                stateDto.getWeightState(),
                stateDto.getWalkingState(),
                stateDto.getSleepingState()
        );
    }
    
}
