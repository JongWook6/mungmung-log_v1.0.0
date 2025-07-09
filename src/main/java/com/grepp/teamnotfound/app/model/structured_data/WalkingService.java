package com.grepp.teamnotfound.app.model.structured_data;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingData;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import com.grepp.teamnotfound.app.model.structured_data.repository.WalkingRepository;
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
public class WalkingService {


    // 산책 정보 리스트 조회
    @Transactional(readOnly = true)
    public List<WalkingData> getWalkingList(Pet pet, LocalDate recordedAt){
        List<Walking> walkingList = walkingRepository.findAllByPetAndRecordedAt(pet, recordedAt);

        if(walkingList.isEmpty()) return List.of();

        return walkingList.stream().map(walking ->
            WalkingData.builder()
                .walkingId(walking.getWalkingId())
                .startedAt(walking.getStartedAt())
                .endedAt(walking.getEndedAt())
                .pace(walking.getPace())
                .build()).collect(Collectors.toList());
    }

}
