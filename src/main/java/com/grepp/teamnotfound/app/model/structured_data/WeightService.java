package com.grepp.teamnotfound.app.model.structured_data;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.WeightData;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.dto.WeightDto;
import com.grepp.teamnotfound.app.model.structured_data.entity.Weight;
import com.grepp.teamnotfound.app.model.structured_data.repository.WeightRepository;
import com.grepp.teamnotfound.infra.error.exception.StructuredDataException;
import com.grepp.teamnotfound.infra.error.exception.code.SleepingErrorCode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeightService {


    // 몸무게 정보 조회
    @Transactional(readOnly = true)
    public WeightData getWeight(Pet pet, LocalDate recordedAt){
        Weight weight = weightRepository.findByPetAndRecordedAt(pet, recordedAt)
                .orElseThrow(() -> new StructuredDataException(SleepingErrorCode.SLEEPING_NOT_FOUND));

        return WeightData.builder()
                .weightId(weight.getWeightId())
                .weight(weight.getWeight())
                .build();
    }
}
