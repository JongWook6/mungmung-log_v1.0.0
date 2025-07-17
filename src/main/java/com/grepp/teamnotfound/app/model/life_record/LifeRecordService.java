package com.grepp.teamnotfound.app.model.life_record;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingData;
import com.grepp.teamnotfound.app.model.life_record.dto.LifeRecordDto;
import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.life_record.repository.LifeRecordRepository;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.structured_data.FeedingService;
import com.grepp.teamnotfound.app.model.structured_data.WalkingService;
import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import com.grepp.teamnotfound.infra.error.exception.LifeRecordException;
import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.code.LifeRecordErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LifeRecordService {

    private final WalkingService walkingService;
    private final FeedingService feedingService;
    private final LifeRecordRepository lifeRecordRepository;
    private final PetRepository petRepository;

    // 생활기록 등록
    @Transactional
    public Long createLifeRecord(LifeRecordDto dto){
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));

        LifeRecord lifeRecord = new LifeRecord();
        lifeRecord.setPet(pet);
        lifeRecord.setRecordedAt(dto.getRecordAt());
        lifeRecord.setContent(dto.getContent());
        lifeRecord.setWeight(dto.getWeight());
        lifeRecord.setSleepingTime(dto.getSleepTime());

        dto.getWalkingList().forEach(walkingDto -> {
            Walking walking = new Walking();
            walking.setStartTime(walkingDto.getStartTime());
            walking.setEndTime(walkingDto.getEndTime());
            walking.setPace(walkingDto.getPace());
            lifeRecord.addWalking(walking);
        });

        dto.getFeedingList().forEach(feedingDto -> {
            Feeding feeding = new Feeding();
            feeding.setMealTime(feedingDto.getMealTime());
            feeding.setAmount(feedingDto.getAmount());
            feeding.setUnit(feedingDto.getUnit());
            lifeRecord.addFeeding(feeding);
        });

        LifeRecord savedLifeRecord = lifeRecordRepository.save(lifeRecord);
        return savedLifeRecord.getLifeRecordId();
    }

    // 생활기록 조회
    @Transactional(readOnly = true)
    public LifeRecordData getLifeRecord(Long lifeRecordId){
        LifeRecord lifeRecord = lifeRecordRepository.findByLifeRecordId(lifeRecordId)
                .orElseThrow(() -> new LifeRecordException(LifeRecordErrorCode.LIFERECORD_NOT_FOUND));

        List<WalkingData> walkingList = lifeRecord.getWalkingList().stream()
                .map(walking -> WalkingData.builder()
                        .startTime(walking.getStartTime().toLocalDateTime())
                        .endTime(walking.getEndTime().toLocalDateTime())
                        .pace(walking.getPace())
                        .build()).toList();
        List<FeedingData> feedingList = lifeRecord.getFeedingList().stream()
                .map(feeding -> FeedingData.builder()
                        .mealtime(feeding.getMealTime().toLocalDateTime())
                        .amount(feeding.getAmount())
                        .unit(feeding.getUnit())
                        .build()).toList();

        return LifeRecordData.builder()
                .lifeRecordId(lifeRecord.getLifeRecordId())
                .petId(lifeRecord.getPet().getPetId())
                .recordAt(lifeRecord.getRecordedAt())
                .content(lifeRecord.getContent())
                .weight(lifeRecord.getWeight())
                .sleepTime(lifeRecord.getSleepingTime())
                .walkingList(walkingList)
                .feedingList(feedingList)
                .build();
    }

    // 생활기록 존재하는지 체크
    @Transactional(readOnly = true)
    public Optional<Long> findLifeRecordId(Long petId, LocalDate date) {
        return lifeRecordRepository.findLifeRecordId(petId, date);
    }

    // 생활기록 수정
    @Transactional
    public void updateLifeRecord(Long lifeRecordId, LifeRecordDto dto){
        LifeRecord lifeRecord = lifeRecordRepository.findByLifeRecordId(lifeRecordId)
                .orElseThrow(() -> new LifeRecordException(LifeRecordErrorCode.LIFERECORD_NOT_FOUND));

        lifeRecord.setContent(dto.getContent());
        lifeRecord.setWeight(dto.getWeight());
        lifeRecord.setSleepingTime(dto.getSleepTime());
        lifeRecord.setUpdatedAt(OffsetDateTime.now());

        walkingService.updateWalkingList(lifeRecord, dto.getWalkingList());
        feedingService.updateFeedingList(lifeRecord, dto.getFeedingList());
    }

    // 생활기록 삭제
    @Transactional
    public void deleteLifeRecord(Long lifeRecordId){
        LifeRecord lifeRecord = lifeRecordRepository.findByLifeRecordId(lifeRecordId)
                .orElseThrow(() -> new LifeRecordException(LifeRecordErrorCode.LIFERECORD_NOT_FOUND));

        lifeRecord.setDeletedAt(OffsetDateTime.now());
        walkingService.deleteWalkingList(lifeRecordId);
        feedingService.deleteFeedingList(lifeRecordId);
    }

}
