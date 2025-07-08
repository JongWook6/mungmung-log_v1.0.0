package com.grepp.teamnotfound.app.controller.api.life_record;

import com.grepp.teamnotfound.app.controller.api.life_record.payload.FeedingResponse;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordListResponse;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordResponse;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.NoteResponse;
import com.grepp.teamnotfound.app.controller.api.life_record.payload.WalkingResponse;
import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/life-record")
public class LifeRecordApiController {

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
    public ResponseEntity<Map<String, LifeRecordResponse>> getLifeRecordDetail(
        @PathVariable Long petId,
        @PathVariable LocalDate date
    ){
        // TODO: 상세정보 불러오는 Service 구현

        // Mock Data
        LifeRecordResponse response = new LifeRecordResponse();
        response.setPetId(petId);
        response.setRecordAt(date);
        response.setNote(NoteResponse.builder().noteId(1L).content("관찰일지 내용").build());
        response.setSleepTime(10);
        response.setWeight(36.3);

        WalkingResponse walking1 = new WalkingResponse();
        walking1.setStartedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(10, 30, 0), OffsetDateTime.now().getOffset()));
        walking1.setEndedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(11, 30, 0), OffsetDateTime.now().getOffset()));
        WalkingResponse walking2 = new WalkingResponse();
        walking2.setStartedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(10, 30, 0), OffsetDateTime.now().getOffset()));
        walking2.setEndedAt(OffsetDateTime.of(LocalDate.of(2025,07,01), LocalTime.of(11, 30, 0), OffsetDateTime.now().getOffset()));
        response.setWalkingList(List.of(walking1, walking2));

        FeedingResponse feeding1 = new FeedingResponse();
        feeding1.setAmount(5.0); feeding1.setMealtime(OffsetDateTime.now()); feeding1.setUnit(FeedUnit.CUP);
        FeedingResponse feeding2 = new FeedingResponse();
        feeding2.setAmount(150.0); feeding2.setMealtime(OffsetDateTime.now()); feeding2.setUnit(FeedUnit.GRAM);
        FeedingResponse feeding3 = new FeedingResponse();
        feeding3.setAmount(20.0); feeding3.setMealtime(OffsetDateTime.now()); feeding3.setUnit(FeedUnit.SCOOP);
        response.setFeedingList(List.of(feeding1, feeding2, feeding3));

        return ResponseEntity.ok(Map.of("data", response));
    }

}
