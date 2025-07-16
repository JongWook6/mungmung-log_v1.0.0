package com.grepp.teamnotfound.app.model.structured_data;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingData;
import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
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

    private final ModelMapper modelMapper;
    private final WalkingRepository walkingRepository;

    // 산책 정보 생성
    @Transactional
    public void createWalking(List<WalkingDto> walkingDtoList){
        for(WalkingDto walkingDto : walkingDtoList){
            Walking walking = modelMapper.map(walkingDto, Walking.class);
            walkingRepository.save(walking);
        }
    }

    // 산책 정보 리스트 조회
    @Transactional(readOnly = true)
    public List<WalkingData> getWalkingList(Long petId, LocalDate recordedAt){
        List<Walking> walkingList = walkingRepository.findWalkingList(petId, recordedAt);

        if(walkingList.isEmpty()) return List.of();

        return walkingList.stream().map(walking ->
            WalkingData.builder()
                .startTime(walking.getStartTime())
                .endTime(walking.getEndTime())
                .pace(walking.getPace())
                .build()).collect(Collectors.toList());
    }

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

    // 산책 정보 삭제
    @Transactional
    public void deleteWalkingList(Long petId, LocalDate recordedAt){
        walkingRepository.delete(petId, recordedAt);
    }

}
