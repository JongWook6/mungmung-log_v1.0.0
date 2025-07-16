package com.grepp.teamnotfound.app.model.structured_data;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
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

    private final ModelMapper modelMapper;
    private final FeedingRepository feedingRepository;

    // 식사 정보 등록
    @Transactional
    public void createFeeding(List<FeedingDto> feedingDtoList){
        for(FeedingDto feedingDto : feedingDtoList){
            Feeding feeding = modelMapper.map(feedingDto, Feeding.class);
            feedingRepository.save(feeding);
        }
    }

    // 식사 정보 조회
    @Transactional(readOnly = true)
    public List<FeedingData> getFeedingList(Long petId, LocalDate recordedAt){
        List<Feeding> feedingList = feedingRepository.findFeedingList(petId, recordedAt);

        if(feedingList.isEmpty()) return List.of();

        return feedingList.stream().map(feeding ->
            FeedingData.builder()
                .amount(feeding.getAmount())
                .mealtime(feeding.getMealTime())
                .unit(feeding.getUnit())
                .build()).collect(Collectors.toList());
    }

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
    public void deleteFeedingList(Long petId, LocalDate recordedAt){
        feedingRepository.delete(petId, recordedAt);
    }

}

