package com.grepp.teamnotfound.app.model.dashboard;

import com.grepp.teamnotfound.app.controller.api.dashboard.payload.DashboardRequest;
import com.grepp.teamnotfound.app.model.dashboard.dto.FeedingDashboardDto;
import com.grepp.teamnotfound.app.model.dashboard.dto.WeightDashboardDto;
import com.grepp.teamnotfound.app.model.note.NoteService;
import com.grepp.teamnotfound.app.model.note.dto.NoteDto;
import com.grepp.teamnotfound.app.model.pet.dto.PetDto;
import com.grepp.teamnotfound.app.model.recommend.RecommendService;
import com.grepp.teamnotfound.app.model.structured_data.FeedingService;
import com.grepp.teamnotfound.app.model.structured_data.SleepingService;
import com.grepp.teamnotfound.app.model.structured_data.WalkingService;
import com.grepp.teamnotfound.app.model.structured_data.WeightService;
import com.grepp.teamnotfound.app.model.structured_data.dto.SleepingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final NoteService noteService;
    private final WeightService weightService;
    private final SleepingService sleepingService;
    private final WalkingService walkingService;
    private final FeedingService feedingService;
    private final RecommendService recommendService;

    public String getRecommend(Long petId, Long userId, LocalDate date) {
        
    }

    public PetDto getProfile(String petId, Long userId, LocalDate date) {
    }

    public FeedingDashboardDto getFeeding(String petId, Long userId, LocalDate date) {
    }

    public NoteDto getNote(String petId, Long userId, LocalDate date) {
    }

    public List<WalkingDto> getWalking(String petId, Long userId, LocalDate date) {
    }

    public WeightDashboardDto getWeight(String petId, Long userId, LocalDate date) {
    }

    public List<SleepingDto> getSleeping(String petId, Long userId, LocalDate date) {
    }
}
