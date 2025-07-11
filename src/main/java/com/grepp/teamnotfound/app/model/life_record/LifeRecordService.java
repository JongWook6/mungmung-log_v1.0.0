package com.grepp.teamnotfound.app.model.life_record;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.NoteData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.SleepingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.WeightData;
import com.grepp.teamnotfound.app.model.life_record.dto.LifeRecordDto;
import com.grepp.teamnotfound.app.model.note.NoteService;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.FeedingService;
import com.grepp.teamnotfound.app.model.structured_data.SleepingService;
import com.grepp.teamnotfound.app.model.structured_data.WalkingService;
import com.grepp.teamnotfound.app.model.structured_data.WeightService;
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

    // 생활기록 등록
    @Transactional
    public void createLifeRecord(LifeRecordDto dto){
        noteService.createNote(dto.getNote());
        sleepingService.createSleeping(dto.getSleepTime());
        weightService.createWeight(dto.getWeight());
        walkingService.createWalking(dto.getWalkingList());
        feedingService.createFeeding(dto.getFeedingList());
    }

    // 생활기록 조회
    @Transactional(readOnly = true)
    public void getLifeRecord(){

    }

    // 생활기록 수정
    @Transactional
    public void updateLifeRecord(){

    }

    // 생활기록 삭제
    @Transactional
    public void deleteLifeRecord(){

    }

}
