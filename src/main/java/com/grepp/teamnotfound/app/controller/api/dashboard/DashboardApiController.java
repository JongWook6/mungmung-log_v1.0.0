package com.grepp.teamnotfound.app.controller.api.dashboard;

import com.grepp.teamnotfound.app.model.ai_analysis.AiAnalysisService;
import com.grepp.teamnotfound.app.controller.api.dashboard.payload.FeedingResponse;
import com.grepp.teamnotfound.app.controller.api.dashboard.payload.NoteResponse;
import com.grepp.teamnotfound.app.controller.api.dashboard.payload.ProfileResponse;
import com.grepp.teamnotfound.app.controller.api.dashboard.payload.SleepingResponse;
import com.grepp.teamnotfound.app.controller.api.dashboard.payload.WalkingResponse;
import com.grepp.teamnotfound.app.controller.api.dashboard.payload.WeightResponse;
import com.grepp.teamnotfound.app.model.ai_analysis.entity.AiAnalysis;
import com.grepp.teamnotfound.app.model.auth.domain.Principal;
import com.grepp.teamnotfound.app.model.dashboard.DashboardService;
import com.grepp.teamnotfound.app.model.dashboard.dto.FeedingDashboardDto;
import com.grepp.teamnotfound.app.model.dashboard.dto.SleepingDashboardDto;
import com.grepp.teamnotfound.app.model.dashboard.dto.WalkingDashboardDto;
import com.grepp.teamnotfound.app.model.dashboard.dto.WeightDashboardDto;
import com.grepp.teamnotfound.app.model.pet.dto.PetDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.grepp.teamnotfound.app.model.recommend.GeminiService;
import com.grepp.teamnotfound.app.model.recommend.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/dashboard")
@PreAuthorize("isAuthenticated()")
public class DashboardApiController {

    private final DashboardService dashboardService;
    private final AiAnalysisService aiAnalysisService;
    private final GeminiService geminiService;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/{petId}/dog-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getDashboardProfile(
            @PathVariable Long petId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal Principal principal
    ){
        PetDto petDto = dashboardService.getProfile(petId, principal.getUserId());
        ProfileResponse response = modelMapper.map(petDto, ProfileResponse.class);
        response.setAge(Period.between(petDto.getBirthday(), date).getMonths());

        String analysis = aiAnalysisService.getAiAnalysis(petId, date);
        if (analysis == null) {
            List<String> notes = dashboardService.getWeekNotes(petId, date);
            GeminiResponse geminiResponse = geminiService.generateAnalysis(notes);
            AiAnalysis aiAnalysis = aiAnalysisService.createAnalysis(petId, geminiResponse);
            analysis = aiAnalysis.getContent();
        }

        response.setAiAnalysis(analysis);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}/feeding")
    public ResponseEntity<?> getDashboardFeeding(
            @PathVariable Long petId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal Principal principal
    ){
        FeedingDashboardDto feedingDashboardDto = dashboardService.getFeeding(petId, principal.getUserId(), date);
        FeedingResponse response = modelMapper.map(feedingDashboardDto, FeedingResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}/note")
    public ResponseEntity<?> getDashboardNote(
            @PathVariable Long petId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal Principal principal
    ){
        String note = dashboardService.getNote(petId, principal.getUserId(), date);
        NoteResponse response = NoteResponse.builder().content(note).date(date).build();
        return ResponseEntity.ok(response);
    }

    // 산책시간 9일전 데이터까지만 받아오기
    @GetMapping("/{petId}/walking")
    public ResponseEntity<?> getDashboardWalking(
            @PathVariable Long petId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal Principal principal
    ){
        WalkingDashboardDto walkingDtos = dashboardService.getWalking(petId, principal.getUserId(), date);
        WalkingResponse response = modelMapper.map(walkingDtos, WalkingResponse.class);
        return ResponseEntity.ok(response);
    }

    // 10개 받아오기
    @GetMapping("/{petId}/weight")
    public ResponseEntity<?> getDashboardWeight(
            @PathVariable Long petId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal Principal principal
    ){
        WeightDashboardDto weightDashboardDto = dashboardService.getWeight(petId, principal.getUserId(), date);
        WeightResponse response = modelMapper.map(weightDashboardDto, WeightResponse.class);
        return ResponseEntity.ok(response);
    }

    // 10개 받아오기
    @GetMapping("/{petId}/sleeping")
    public ResponseEntity<?> getDashboardSleeping(
            @PathVariable Long petId,
            @RequestParam LocalDate date,
            @AuthenticationPrincipal Principal principal
    ){
        SleepingDashboardDto sleepingDtos = dashboardService.getSleeping(petId, principal.getUserId(), date);
        SleepingResponse response = modelMapper.map(sleepingDtos, SleepingResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}/checklist")
    public ResponseEntity<?> getDashboardChecklist(
        @PathVariable Long petId,
        @RequestParam LocalDate date,
        @AuthenticationPrincipal Principal principal
    ){
        return ResponseEntity.ok(dashboardService.getChecklist(petId, principal.getUserId(), date));
    }
}
