package com.grepp.teamnotfound.app.model.structured_data;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.dto.FeedingDto;
import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import com.grepp.teamnotfound.app.model.structured_data.repository.FeedingRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedingService {

    private final FeedingRepository feedingRepository;

    // 식사 정보 수정
    @Transactional
    public void updateFeedingList(LifeRecord lifeRecord, List<FeedingDto> newFeedingDtoList) {
        List<Feeding> originFeedingList = lifeRecord.getFeedingList();
        int originSize = originFeedingList.size();
        int newSize = newFeedingDtoList.size();

        int minSize = Math.min(originSize, newSize);
        for (int i = 0; i < minSize; i++) { // 기존 데이터 수정
            Feeding originFeeding = originFeedingList.get(i);
            FeedingDto newFeedingDto = newFeedingDtoList.get(i);

            originFeeding.setAmount(newFeedingDto.getAmount());
            originFeeding.setMealTime(newFeedingDto.getMealTime());
            originFeeding.setUnit(newFeedingDto.getUnit());
            originFeeding.setUpdatedAt(OffsetDateTime.now());
        }

        if (originSize > newSize) { // 기존 리스트가 더 큰 경우: 남는 데이터 soft delete 처리
            for (int i = newSize; i < originSize; i++) {
                originFeedingList.get(i).setDeletedAt(OffsetDateTime.now());
            }
        } else if (originSize < newSize) { // 새로운 리스트가 더 큰 경우: 새로운 데이터 추가
            for (int i = originSize; i < newSize; i++) {
                FeedingDto newFeedingDto = newFeedingDtoList.get(i);

                Feeding newFeeding = newFeedingDto.toEntity();
                newFeeding.setUpdatedAt(OffsetDateTime.now());
                newFeeding.setLifeRecord(lifeRecord);

                lifeRecord.addFeeding(newFeeding);
            }
        }
    }

    // 식사 정보 삭제
    @Transactional
    public void deleteFeedingList(Long lifeRecordId){
        feedingRepository.delete(lifeRecordId);
    }

    // 일주일 간 평균 식사량
    @Transactional
    public Double getFeedingAverage(List<LifeRecord> lifeRecords) {
        if (lifeRecords.isEmpty()) return 0.0;

        Double amount = 0.0;
        // lifeRecords 순회하며 식사량 더하기
        for (LifeRecord lifeRecord: lifeRecords){
            for(Feeding feeding: lifeRecord.getFeedingList()){
                amount += feeding.getAmount();
            }
        }

        return amount/lifeRecords.size();
    }

    public Map<LocalDate, List<Feeding>> getFeedingList(Map<Long, LocalDate> lifeRecordIds) {
        List<Long> ids = new ArrayList<>(lifeRecordIds.keySet());
        List<Feeding> feedings = feedingRepository.findAllByLifeRecord_LifeRecordIdIn(ids);

        // 기록일 별로 정리
        Map<LocalDate, List<Feeding>> result = new HashMap<>();
        for (Feeding feeding : feedings) {
            Long lifeRecordId = feeding.getLifeRecord().getLifeRecordId();
            LocalDate recordedDate = lifeRecordIds.get(lifeRecordId);

            result.computeIfAbsent(recordedDate, k -> new ArrayList<>()).add(feeding);
        }

        return result;
    }
}

