package com.grepp.teamnotfound.app.model.structured_data;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.dto.FeedingDto;
import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import com.grepp.teamnotfound.app.model.structured_data.repository.FeedingRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedingService {


    // 식사 정보 조회
    @Transactional(readOnly = true)
    public List<FeedingData> getFeedingList(Pet pet, LocalDate recordedAt){
        List<Feeding> feedingList = feedingRepository.findAllByPetAndRecordedAt(pet, recordedAt);

        if(feedingList.isEmpty()) return List.of();

        return feedingList.stream().map(feeding ->
            FeedingData.builder()
                .feedingId(feeding.getFeedingId())
                .amount(feeding.getAmount())
                .mealtime(feeding.getMealTime())
                .unit(feeding.getUnit())
                .build()).collect(Collectors.toList());
    }

}

