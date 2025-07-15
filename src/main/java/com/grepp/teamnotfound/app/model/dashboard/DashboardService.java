package com.grepp.teamnotfound.app.model.dashboard;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.model.dashboard.dto.FeedingDashboardDto;
import com.grepp.teamnotfound.app.model.note.NoteService;
import com.grepp.teamnotfound.app.model.note.dto.NoteDto;
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
import com.grepp.teamnotfound.infra.error.exception.UserException;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        return null;
    }

    public List<WalkingDto> getWalking(Long petId, Long userId, LocalDate date) {
    }

    public List<WeightDto> getWeight(Long petId, Long userId, LocalDate date) {
    }

    public List<SleepingDto> getSleeping(Long petId, Long userId, LocalDate date) {
    }
}
