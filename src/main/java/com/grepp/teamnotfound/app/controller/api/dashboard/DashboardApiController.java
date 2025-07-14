package com.grepp.teamnotfound.app.controller.api.dashboard;

import com.grepp.teamnotfound.app.controller.api.dashboard.payload.*;
import com.grepp.teamnotfound.app.model.dashboard.DashboardService;
import com.grepp.teamnotfound.app.model.dashboard.dto.FeedingDashboardDto;
import com.grepp.teamnotfound.app.model.dashboard.dto.WeightDashboardDto;
import com.grepp.teamnotfound.app.model.note.dto.NoteDto;
import com.grepp.teamnotfound.app.model.pet.dto.PetDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.SleepingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/dashboard")
public class DashboardApiController {

    private final DashboardService dashboardService;
    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/{petId}/recommend")
    public ResponseEntity<?> getDashboardRecommend(
            @PathVariable Long petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
            ){
        String recommend = dashboardService.getRecommend(petId, userId, date);

        return ResponseEntity.ok(Map.of("recommend", recommend));
    }

    // 프로필에 견종 추가
    @GetMapping("/{petId}/dog-profile")
    public ResponseEntity<?> getDashboardProfile(
            @PathVariable String petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
    ){
        PetDto petDto = dashboardService.getProfile(petId, userId, date);
        ProfileResponse response = modelMapper.map(petDto, ProfileResponse.class);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}/feeding")
    public ResponseEntity<?> getDashboardFeeding(
            @PathVariable String petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
    ){
        FeedingDashboardDto feedingDashboardDto = dashboardService.getFeeding(petId, userId, date);
        FeedingResponse response = modelMapper.map(feedingDashboardDto, FeedingResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}/note")
    public ResponseEntity<?> getDashboardNote(
            @PathVariable String petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
    ){
        NoteDto noteDto = dashboardService.getNote(petId, userId, date);
        NoteResponse response = modelMapper.map(noteDto, NoteResponse.class);
        return ResponseEntity.ok(response);
    }

    // 산책시간 9일전 데이터까지만 받아오기
    @GetMapping("/{petId}/walking")
    public ResponseEntity<?> getDashboardWalking(
            @PathVariable String petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
    ){
        List<WalkingDto> walkingDtos = dashboardService.getWalking(petId, userId, date);
        WalkingResponse response = modelMapper.map(walkingDtos, WalkingResponse.class);
        return ResponseEntity.ok(response);
    }

    // 10개 받아오기
    @GetMapping("/{petId}/weight")
    public ResponseEntity<?> getDashboardWeight(
            @PathVariable String petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
    ){
        WeightDashboardDto weightDashboardDto = dashboardService.getWeight(petId, userId, date);
        WeightResponse response = modelMapper.map(weightDashboardDto, WeightResponse.class);
        return ResponseEntity.ok(response);
    }

    // 10개 받아오기
    @GetMapping("/{petId}/sleeping")
    public ResponseEntity<?> getDashboardSleeping(
            @PathVariable String petId,
            @RequestParam Long userId,
            @RequestParam LocalDate date
    ){
        List<SleepingDto> sleepingDtos = dashboardService.getSleeping(petId, userId, date);
        SleepingResponse response = modelMapper.map(sleepingDtos, SleepingResponse.class);
        return ResponseEntity.ok(response);
    }
}
