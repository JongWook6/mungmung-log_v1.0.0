package com.grepp.teamnotfound.app.model.liferecord;

import com.grepp.teamnotfound.app.controller.api.liferecord.payload.LifeRecordData;
import com.grepp.teamnotfound.app.controller.api.liferecord.payload.LifeRecordListRequest;
import com.grepp.teamnotfound.app.model.liferecord.dto.LifeRecordDto;
import com.grepp.teamnotfound.app.model.liferecord.dto.LifeRecordListDto;
import com.grepp.teamnotfound.app.model.liferecord.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.liferecord.repository.LifeRecordRepository;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.structured_data.FeedingService;
import com.grepp.teamnotfound.app.model.structured_data.WalkingService;
import com.grepp.teamnotfound.app.model.structured_data.code.FeedUnit;
import com.grepp.teamnotfound.app.model.structured_data.repository.FeedingRepository;
import com.grepp.teamnotfound.app.model.structured_data.repository.WalkingRepository;
import com.grepp.teamnotfound.infra.error.exception.LifeRecordException;
import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.code.LifeRecordErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LifeRecordService {

    private final WalkingService walkingService;
    private final FeedingService feedingService;
    private final LifeRecordRepository lifeRecordRepository;
    private final PetRepository petRepository;
    private final FeedingRepository feedingRepository;
    private final WalkingRepository walkingRepository;

    // 생활기록 등록
    @Transactional
    public Long createLifeRecord(LifeRecordDto dto){
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));

        LifeRecord lifeRecord = LifeRecord.create(pet, dto);
        LifeRecord savedLifeRecord = lifeRecordRepository.save(lifeRecord);

        return savedLifeRecord.getLifeRecordId();
    }

    // 생활기록 조회
    @Transactional(readOnly = true)
    public LifeRecordData getLifeRecord(Long lifeRecordId){
        LifeRecord lifeRecord = lifeRecordRepository.findByLifeRecordId(lifeRecordId)
                .orElseThrow(() -> new LifeRecordException(LifeRecordErrorCode.LIFERECORD_NOT_FOUND));

        return LifeRecordData.of(lifeRecord);
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
        walkingRepository.delete(lifeRecordId);
        feedingRepository.delete(lifeRecordId);
    }

    // 생활기록 리스트 조회
    @Transactional(readOnly = true)
    public Page<LifeRecordListDto> searchLifeRecords(Long userId, LifeRecordListRequest request, Pageable pageable) {
        return lifeRecordRepository.search(userId, request, pageable);
    } 

    @Transactional(readOnly = true)
    public List<LifeRecord> getSleepingLifeRecordList(Pet pet, LocalDate date){
        return lifeRecordRepository.findTop10ByPetAndDeletedAtNullAndRecordedAtBeforeAndSleepingTimeIsNotNullOrderByRecordedAtDesc(pet, date.plusDays(1));
    }

    @Transactional(readOnly = true)
    public List<LifeRecord> getWeightLifeRecordList(Pet pet, LocalDate date) {
        return lifeRecordRepository.findTop10ByPetAndDeletedAtNullAndRecordedAtBeforeAndWeightIsNotNullOrderByRecordedAtDesc(pet, date.plusDays(1));
    }

    @Transactional(readOnly = true)
    public Map<Long, LocalDate> get7LifeRecordList(Pet pet, LocalDate date) {
        List<LifeRecord> lifeRecords = lifeRecordRepository.findByPetAndDeletedAtNullAndRecordedAtBetweenOrderByRecordedAtDesc(pet, date.minusDays(8), date.minusDays(1));
        Map<Long, LocalDate> mapList = new HashMap<>();
        for (LifeRecord lifeRecord : lifeRecords){
            mapList.put(lifeRecord.getLifeRecordId(), lifeRecord.getRecordedAt());
        }
        return mapList;
    }

    @Transactional(readOnly = true)
    public Map<Long, LocalDate> get9LifeRecordList(Pet pet, LocalDate date) {
        List<LifeRecord> lifeRecords = lifeRecordRepository.findByPetAndDeletedAtNullAndRecordedAtBetweenOrderByRecordedAtDesc(pet, date.minusDays(9), date);

        Map<Long, LocalDate> mapList = new HashMap<>();
        for (LifeRecord lifeRecord : lifeRecords){
            mapList.put(lifeRecord.getLifeRecordId(), lifeRecord.getRecordedAt());
        }
        return mapList;
    }

    // 유저가 사용한 최근 식사단위
    @Transactional(readOnly = true)
    public Optional<FeedUnit> getRecentFeedUnit(Long petId) {
        return lifeRecordRepository.findRecentFeedUnit(petId);
    }

    /*
    * Bootify용 Service
    */
    @Transactional(readOnly = true)
    public List<LifeRecordData> findAll() {
        return lifeRecordRepository.findAll().stream()
                .filter(lifeRecord -> lifeRecord.getDeletedAt() == null)
                .map(lifeRecord -> getLifeRecord(lifeRecord.getLifeRecordId()))
                .toList();
    }

}
