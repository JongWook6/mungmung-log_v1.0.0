package com.grepp.teamnotfound.app.controller.api.recommend;

import com.grepp.teamnotfound.app.controller.api.recommend.payload.RecommendResponse;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendApiController {

    private final PetService petService;
    private final RecommendService recommendService;

    @Operation(summary = "반려견 맞춤형 정보 제공")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/v1/pet/{petId}")
    public ResponseEntity<RecommendResponse> getRecommend(
            @PathVariable Long petId
    ) {
        Pet pet = petService.getPet(petId);

        // 기존 Recommend가 있으면 반환
        if(recommendService.existsByPetAndDate(pet)){
            return ResponseEntity.ok(recommendService.getRecommendByPet(pet));
        }

        // Gemini 응답
        GeminiResponse response = recommendService.getGemini(pet);
        // Recommend 생성
        return ResponseEntity.ok(recommendService.createRecommend(pet, response));
    }

}
