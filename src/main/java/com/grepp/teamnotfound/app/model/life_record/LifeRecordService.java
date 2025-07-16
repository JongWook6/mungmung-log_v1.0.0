package com.grepp.teamnotfound.app.model.life_record;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingData;
import com.grepp.teamnotfound.app.model.life_record.dto.LifeRecordDto;
import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.life_record.repository.LifeRecordRepository;
import com.grepp.teamnotfound.app.model.note.NoteService;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.structured_data.FeedingService;
import com.grepp.teamnotfound.app.model.structured_data.SleepingService;
import com.grepp.teamnotfound.app.model.structured_data.WalkingService;
import com.grepp.teamnotfound.app.model.structured_data.WeightService;
import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LifeRecordService {

    private final NoteService noteService;
    private final WeightService weightService;
    private final SleepingService sleepingService;
    private final WalkingService walkingService;
    private final FeedingService feedingService;
    private final LifeRecordRepository lifeRecordRepository;
    private final PetRepository petRepository;

    // 생활기록 등록
    @Transactional
    public void createLifeRecord(LifeRecordDto dto){
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

        lifeRecordRepository.save(lifeRecord);
    }

    // 생활기록 조회
    @Transactional(readOnly = true)
    public LifeRecordData getLifeRecord(Long petId, LocalDate recordedAt){
        String content = noteService.getNote(petId, recordedAt);
        Integer sleepingTime = sleepingService.getSleeping(petId, recordedAt);
        Double weight = weightService.getWeight(petId, recordedAt);
        List<WalkingData> walkingList = walkingService.getWalkingList(petId, recordedAt);
        List<FeedingData> feedingList = feedingService.getFeedingList(petId, recordedAt);

        LifeRecordData lifeRecord = new LifeRecordData();
        lifeRecord.setPetId(petId);
        lifeRecord.setRecordAt(recordedAt);
        lifeRecord.setContent(content);
        lifeRecord.setSleepTime(sleepingTime);
        lifeRecord.setWeight(weight);
        lifeRecord.setWalkingList(walkingList);
        lifeRecord.setFeedingList(feedingList);

        return lifeRecord;
    }

    // 생활기록 수정
    @Transactional
    public void updateLifeRecord(Long petId, LifeRecordDto dto){
        // 기존 데이터 삭제
        deleteLifeRecord(petId, dto.getRecordAt());
        // 생활 기록 수정
        noteService.updateNote(dto.getNote());
        sleepingService.updateSleeping(dto.getSleepTime());
        weightService.updateWeight(dto.getWeight());
        walkingService.updateWalkingList(dto.getWalkingList());
        feedingService.updateFeedingList(dto.getFeedingList());
    }

    // 생활기록 삭제
    @Transactional
    public void deleteLifeRecord(Long petId, LocalDate date){
        noteService.deleteNote(petId, date);
        sleepingService.deleteSleeping(petId, date);
        weightService.deleteWeight(petId, date);
        walkingService.deleteWalkingList(petId, date);
        feedingService.deleteFeedingList(petId, date);
    }

}
