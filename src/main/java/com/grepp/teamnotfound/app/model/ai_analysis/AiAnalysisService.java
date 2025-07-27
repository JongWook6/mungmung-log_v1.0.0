package com.grepp.teamnotfound.app.model.ai_analysis;


import com.grepp.teamnotfound.app.model.ai_analysis.entity.AiAnalysis;
import com.grepp.teamnotfound.app.model.ai_analysis.repository.AiAnalysisRepository;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AiAnalysisService {

    private final AiAnalysisRepository aiAnalysisRepository;
    private final PetRepository petRepository;

    @Transactional
    public String getAiAnalysis(Long petId, LocalDate date) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));

        Optional<AiAnalysis> aiAnalysis = aiAnalysisRepository.findFirstByPetOrderByCreatedAtDesc(pet);
        if (aiAnalysis.isEmpty()){
            return null;
        }

        AiAnalysis analysis = aiAnalysis.get();

        OffsetDateTime oneWeekAgo = date.minusWeeks(1).atStartOfDay().atOffset(ZoneOffset.ofHours(9));

        if(analysis.getCreatedAt().isAfter(oneWeekAgo)){
            return analysis.getContent();
        }

        return null;
    }

    @Transactional
    public AiAnalysis createAnalysis(Long petId, String geminiResponse) {
        AiAnalysis aiAnalysis = AiAnalysis.builder().content(geminiResponse).pet(petRepository.findById(petId).get()).build();
        aiAnalysisRepository.save(aiAnalysis);
        return aiAnalysis;
    }
}

