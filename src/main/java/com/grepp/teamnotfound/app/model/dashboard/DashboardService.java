package com.grepp.teamnotfound.app.model.dashboard;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.model.dashboard.dto.DayWalking;
import com.grepp.teamnotfound.app.model.dashboard.dto.FeedingDashboardDto;
import com.grepp.teamnotfound.app.model.dashboard.dto.WalkingDashboardDto;
import com.grepp.teamnotfound.app.model.note.NoteService;
import com.grepp.teamnotfound.app.model.note.dto.NoteDto;
import com.grepp.teamnotfound.app.model.note.entity.Note;
import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.pet.dto.PetDto;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.DailyRecommendService;
import com.grepp.teamnotfound.app.model.structured_data.FeedingService;
import com.grepp.teamnotfound.app.model.structured_data.SleepingService;
import com.grepp.teamnotfound.app.model.structured_data.WalkingService;
import com.grepp.teamnotfound.app.model.structured_data.WeightService;
import com.grepp.teamnotfound.app.model.structured_data.dto.SleepingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WeightDto;
import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import com.grepp.teamnotfound.infra.error.exception.UserException;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final NoteService noteService;
    private final PetService petService;
    private final WeightService weightService;
    private final SleepingService sleepingService;
    private final WalkingService walkingService;
    private final FeedingService feedingService;
    private final DailyRecommendService dailyRecommendService;

    ModelMapper modelMapper = new ModelMapper();

    public String getRecommend(Long petId, Long userId, LocalDate date) {
        Pet pet = petService.getPet(petId);
        if(pet.getUser().getUserId().equals(userId)) throw new UserException(UserErrorCode.USER_ACCESS_DENIED);

        return dailyRecommendService.getRecommend(pet, date);
    }

    public PetDto getProfile(Long petId, Long userId) {
        Pet pet = petService.getPet(petId);
        if(pet.getUser().getUserId().equals(userId)) throw new UserException(UserErrorCode.USER_ACCESS_DENIED);
        return modelMapper.map(pet, PetDto.class);
    }

    public FeedingDashboardDto getFeeding(Long petId, Long userId, LocalDate date) {
        Pet pet = petService.getPet(petId);
        if(pet.getUser().getUserId().equals(userId)) throw new UserException(UserErrorCode.USER_ACCESS_DENIED);

        List<FeedingData> datas = feedingService.getFeedingList(petId, date);
        Double amount = 0.0;

        for(FeedingData data : datas){
            amount += data.getAmount();
        }

        return FeedingDashboardDto.builder()
                .amount(amount)
                .average(feedingService.getFeedingAverage(pet, date))
                .unit(datas.getFirst().getUnit())
                .date(date)
                .build();
    }

    public NoteDto getNote(Long petId, Long userId, LocalDate date) {
        Pet pet = petService.getPet(petId);
        if(pet.getUser().getUserId().equals(userId)) throw new UserException(UserErrorCode.USER_ACCESS_DENIED);

        Note note = noteService.findNote(petId, date);
        return modelMapper.map(note, NoteDto.class);
    }

    public WalkingDashboardDto getWalking(Long petId, Long userId, LocalDate date) {
        Pet pet = petService.getPet(petId);
        if(pet.getUser().getUserId().equals(userId)) throw new UserException(UserErrorCode.USER_ACCESS_DENIED);

        List<Walking> walkings = walkingService.getWalkingListDays(pet, date);
        WalkingDashboardDto dto = new WalkingDashboardDto();
        Map<LocalDate, DayWalking> dayWalkingMap = new HashMap<>();
        Map<LocalDate, Integer> dayCnt = new HashMap<>();

        // 하루 산책량 총합 계산, 산책 횟수 계산
        for (Walking walking:walkings){
            LocalDate day = walking.getRecordedAt();
            long time = Duration.between(walking.getStartTime(), walking.getEndTime()).toMinutes();

            if (dayWalkingMap.containsKey(day)) {
                DayWalking existing = dayWalkingMap.get(day);
                existing.setTime(existing.getTime() + time);
                existing.setPace(existing.getPace() + walking.getPace());
                dayCnt.replace(day, dayCnt.get(day) + 1);
            } else {
                dayWalkingMap.put(day,
                        DayWalking.builder()
                                .date(walking.getRecordedAt())
                                .time(time)
                                .pace(walking.getPace())
                                .build());
                dayCnt.put(day, 1);
            }
        }

        // 하루 평균 pace 계산
        for (Map.Entry<LocalDate, DayWalking> entry : dayWalkingMap.entrySet()) {
            LocalDate day = entry.getKey();
            DayWalking daily = entry.getValue();

            int cnt = dayCnt.get(day);
            daily.setPace(daily.getPace() / cnt);
        }

        dto.getWalkingList().addAll(dayWalkingMap.values());

        return dto;
    }

    public List<WeightDto> getWeight(Long petId, Long userId, LocalDate date) {
    }

    public List<SleepingDto> getSleeping(Long petId, Long userId, LocalDate date) {
    }
}
