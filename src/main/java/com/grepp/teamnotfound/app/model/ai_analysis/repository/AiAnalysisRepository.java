package com.grepp.teamnotfound.app.model.ai_analysis.repository;

import com.grepp.teamnotfound.app.model.ai_analysis.entity.AiAnalysis;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiAnalysisRepository extends JpaRepository<AiAnalysis, Long> {

    AiAnalysis findFirstByPet(Pet pet);

}

