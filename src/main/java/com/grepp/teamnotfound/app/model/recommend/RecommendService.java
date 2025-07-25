package com.grepp.teamnotfound.app.model.recommend;

import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendDto;
import com.grepp.teamnotfound.app.model.recommend.dto.LifeRecordAvgDto;
import com.grepp.teamnotfound.app.model.recommend.dto.LifeRecordListDto;
import com.grepp.teamnotfound.app.model.recommend.dto.PetInfoDto;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendCheckDto;
import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.life_record.repository.LifeRecordRepository;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.dto.RecommendStateDto;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import com.grepp.teamnotfound.app.model.recommend.repository.RecommendRepository;
import com.grepp.teamnotfound.app.model.recommend.repository.StandardRepository;
import com.grepp.teamnotfound.infra.error.exception.StandardException;
import com.grepp.teamnotfound.infra.error.exception.code.StandardErrorCode;
import java.util.List;
import java.util.Optional;
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

    // 반려견의 종+나이, 최근 10일 생활기록 평균 데이터 생성
    @Transactional(readOnly = true)
    public RecommendCheckDto getRecommendCheck(Pet pet){
        Integer age = pet.getAge(pet.getBirthday());
        Standard standard = standardRepository.findStandardByBreedAndAge(pet.getBreed(), age)
                .orElseThrow(() -> new StandardException(StandardErrorCode.STANDARD_NOT_FOUND));

        List<LifeRecord> lifeRecordList = lifeRecordRepository.findTop10ByPet(pet);
        LifeRecordListDto listDto = LifeRecordListDto.toDto(lifeRecordList);
        LifeRecordAvgDto avgDto = LifeRecordAvgDto.toDto(lifeRecordList);
        RecommendStateDto stateDto = RecommendStateDto.toDto(avgDto, standard);
        PetInfoDto petInfoDto = PetInfoDto.toDto(standard);

        return RecommendCheckDto.builder()
                .listDto(listDto)
                .avgDto(avgDto)
                .stateDto(stateDto)
                .petInfoDto(petInfoDto)
                .build();
    }

    // 기존 Recommend 여부 체크
    @Transactional(readOnly = true)
    public Optional<Recommend> getRecommendByPetStates(RecommendCheckDto checkDto) {
        return recommendRepository.findRecommendByAllStates(
                checkDto.getPetInfoDto().getBreed(),
                checkDto.getPetInfoDto().getAge(),
                checkDto.getStateDto().getWeightState(),
                checkDto.getStateDto().getSleepingState(),
                checkDto.getStateDto().getSleepingState()
        );
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

}
