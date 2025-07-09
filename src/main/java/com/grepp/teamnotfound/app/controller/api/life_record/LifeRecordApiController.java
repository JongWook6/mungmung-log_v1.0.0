package com.grepp.teamnotfound.app.controller.api.life_record;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordListResponse;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.NoteData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.SleepingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingData;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.WeightData;
import com.grepp.teamnotfound.app.model.note.NoteService;
import com.grepp.teamnotfound.app.model.note.dto.NoteDto;
import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import com.grepp.teamnotfound.app.model.structured_data.FeedingService;
import com.grepp.teamnotfound.app.model.structured_data.SleepingService;
import com.grepp.teamnotfound.app.model.structured_data.WalkingService;
import com.grepp.teamnotfound.app.model.structured_data.WeightService;
import com.grepp.teamnotfound.app.model.structured_data.dto.FeedingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.SleepingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WalkingDto;
import com.grepp.teamnotfound.app.model.structured_data.dto.WeightDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/life-record")
public class LifeRecordApiController {

    private final PetService petService;
    private final SleepingService sleepingService;
    private final WeightService weightService;
    private final NoteService noteService;
    private final WalkingService walkingService;
    private final FeedingService feedingService;

    // 보호자의 반려견 생활기록 리스트 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, List<LifeRecordListResponse>>> getLifeRecordList(
            @PathVariable Long userId,
            @RequestParam(required = false) Long petId,
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        // TODO: 생활기록 리스트 조회 Service 구현

        // Mock Data
        LifeRecordListResponse record1 = LifeRecordListResponse.builder()
                .createdAt(OffsetDateTime.now())
                .noteId(1L).name("이마음").savePath("이미지 저장경로").weight(36.3F)
                .content("더워서 기분 안 좋아 보임").build();
        LifeRecordListResponse record2 = LifeRecordListResponse.builder()
                .createdAt(OffsetDateTime.now())
                .noteId(1L).name("이마음").savePath("이미지 저장경로").weight(36.3F)
                .content("더워서 기분 안 좋아 보임").build();
        LifeRecordListResponse record3 = LifeRecordListResponse.builder()
                .createdAt(OffsetDateTime.now())
                .noteId(1L).name("이마음").savePath("이미지 저장경로").weight(36.3F)
                .content("더워서 기분 안 좋아 보임").build();

        List<LifeRecordListResponse> list = new ArrayList<>(List.of(record1, record2, record3));

        return ResponseEntity.ok(Map.of("data", list));
    }

    // 보호자의 반려견 목록 조회 (특정 반려견 생활기록만 보기 위하여 필요)
    @GetMapping("/{userId}/pet-list")
    public ResponseEntity<Map<String, List<Map<Long, String>>>> getPetList(
            @PathVariable Long userId
    ){
        // TODO: 반려견 목록 조회 Service 구현

        // Mock Data
        Map<Long, String> pet1 = new HashMap<>();
        pet1.put(1L, "이마음");
        Map<Long, String> pet2 = new HashMap<>();
        pet2.put(2L, "김행복");
        Map<Long, String> pet3 = new HashMap<>();
        pet3.put(3L, "박슬픔");

        List<Map<Long, String>> list = new ArrayList<>(List.of(pet1, pet2, pet3));

        return ResponseEntity.ok(Map.of("data",list));
    }

    // 생활기록 상세정보 조회
    @GetMapping("/{petId}/{date}")
    public ResponseEntity<Map<String, LifeRecordData>> getLifeRecordDetail(
        @PathVariable Long petId,
        @PathVariable LocalDate date
    ){
        // 상세정보 조회 Service
        // LifeRecordData lifeRecord = findLifeRecord(petId, date);

        // Mock Data
        LifeRecordData response = new LifeRecordData();
        response.setPetId(petId);
        response.setRecordAt(date);
        response.setNote(NoteData.builder().noteId(1L).content("관찰일지 내용").build());
        SleepingData sleeping = SleepingData.builder()
                .sleepingId(1L).sleepTime(10).build();
        response.setSleepTime(sleeping);
        WeightData weight = WeightData.builder()
                .weightId(1L).weight(10.0).build();
        response.setWeight(weight);

        WalkingData walking1 = WalkingData.builder().walkingId(1L)
                .startedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(10, 30, 0), OffsetDateTime.now().getOffset()))
                .endedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(11, 30, 0), OffsetDateTime.now().getOffset())).build();
        WalkingData walking2 = WalkingData.builder().walkingId(1L)
                .startedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(10, 30, 0), OffsetDateTime.now().getOffset()))
                .endedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(11, 30, 0), OffsetDateTime.now().getOffset())).build();
        response.setWalkingList(List.of(walking1, walking2));

        FeedingData feeding1 = FeedingData.builder()
                .feedingId(1L).amount(5.0).mealtime(OffsetDateTime.now()).unit(FeedUnit.CUP).build();
        FeedingData feeding2 = FeedingData.builder()
                .feedingId(2L).amount(150.0).mealtime(OffsetDateTime.now()).unit(FeedUnit.GRAM).build();
        FeedingData feeding3 = FeedingData.builder()
                .feedingId(3L).amount(20.0).mealtime(OffsetDateTime.now()).unit(FeedUnit.SCOOP).build();
        response.setFeedingList(List.of(feeding1, feeding2, feeding3));

        return ResponseEntity.ok(Map.of("data", response));
    }


    // 생활기록 데이터 조회 및 합치기
    private LifeRecordData findLifeRecord(Long petId, LocalDate date){
        Pet pet = petService.getPet(petId);
        // 관찰노드 조회
        NoteData note = noteService.getNote(pet, date);

        // 수면 조회
        SleepingData sleeping = sleepingService.getSleeping(pet, date);

        // 몸무게 조회
        WeightData weight = weightService.getWeight(pet, date);

        // 산책 조회
        List<WalkingData> walkingList = walkingService.getWalkingList(pet, date);

        // 식사 조회
        List<FeedingData> feedingList = feedingService.getFeedingList(pet, date);

        // 전체 데이터 합치기
        LifeRecordData lifeRecord = new LifeRecordData();
        lifeRecord.setPetId(petId);
        lifeRecord.setRecordAt(date);
        lifeRecord.setNote(note);
        lifeRecord.setSleepTime(sleeping);
        lifeRecord.setWeight(weight);
        lifeRecord.setWalkingList(walkingList);
        lifeRecord.setFeedingList(feedingList);

        return lifeRecord;
    }
}
