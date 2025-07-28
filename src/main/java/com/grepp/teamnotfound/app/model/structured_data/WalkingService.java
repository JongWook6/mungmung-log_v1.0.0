package com.grepp.teamnotfound.app.model.structured_data;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import com.grepp.teamnotfound.app.model.structured_data.repository.WalkingRepository;

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
public class WalkingService {

    private final WalkingRepository walkingRepository;

    // 산책 정보 수정
    @Transactional
    public void updateWalkingList(LifeRecord lifeRecord, List<WalkingDto> newWalkingDtoList) {
        List<Walking> originWalkingList = lifeRecord.getWalkingList();
        int originSize = originWalkingList.size();
        int newSize = newWalkingDtoList.size();

        int minSize = Math.min(originSize, newSize);
        for (int i = 0; i < minSize; i++) { // 기존 데이터 수정
            Walking originWalking = originWalkingList.get(i);
            WalkingDto newWalkingDto = newWalkingDtoList.get(i);

            originWalking.setStartTime(newWalkingDto.getStartTime());
            originWalking.setEndTime(newWalkingDto.getEndTime());
            originWalking.setPace(newWalkingDto.getPace());
            originWalking.setUpdatedAt(OffsetDateTime.now());
        }

        if (originSize > newSize) { // 기존 리스트가 더 큰 경우: 남는 데이터 soft delete 처리
            for (int i = newSize; i < originSize; i++) {
                originWalkingList.get(i).setDeletedAt(OffsetDateTime.now());
            }
        } else { // 새로운 리스트가 더 큰 경우: 새로운 데이터 추가
            for (int i = originSize; i < newSize; i++) {
                WalkingDto newWalkingDto = newWalkingDtoList.get(i);

                Walking newWalking = newWalkingDto.toEntity();
                newWalking.setUpdatedAt(OffsetDateTime.now());
                newWalking.setLifeRecord(lifeRecord);

                lifeRecord.addWalking(newWalking);
            }
        }
    }

    @Transactional(readOnly = true)
    public Map<LocalDate, List<Walking>> getWalkingList(Map<Long, LocalDate> lifeRecordIds) {
        List<Long> ids = new ArrayList<>(lifeRecordIds.keySet());
        List<Walking> walkings = walkingRepository.findAllByLifeRecord_LifeRecordIdIn(ids);

        // 기록일 별로 정리
        Map<LocalDate, List<Walking>> result = new HashMap<>();
        for (Walking walking : walkings) {
            Long lifeRecordId = walking.getLifeRecord().getLifeRecordId();
            LocalDate recordedDate = lifeRecordIds.get(lifeRecordId);

            result.computeIfAbsent(recordedDate, k -> new ArrayList<>()).add(walking);
        }

        return result;
    }
}
