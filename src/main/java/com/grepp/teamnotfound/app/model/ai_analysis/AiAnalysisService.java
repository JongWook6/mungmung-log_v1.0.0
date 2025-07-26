package com.grepp.teamnotfound.app.model.ai_analysis;


import com.grepp.teamnotfound.app.model.ai_analysis.entity.AiAnalysis;
import com.grepp.teamnotfound.app.model.ai_analysis.repository.AiAnalysisRepository;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AiAnalysisService {

    private final AiAnalysisRepository aiAnalysisRepository;
    private final PetRepository petRepository;

    @Transactional
    public String getAiAnalysis(Long petId, LocalDate date) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));

        Optional<AiAnalysis> aiAnalysis = aiAnalysisRepository.findFirstByPetOrderByCreatedAt(pet, date);
        if (aiAnalysis.isEmpty()){
            return null;
        }

        AiAnalysis analysis = aiAnalysis.get();

        if(analysis.getCreatedAt().isAfter(OffsetDateTime.from(date.minusWeeks(1)))){
            return analysis.getContent();
        }

        return null;
    }
}

