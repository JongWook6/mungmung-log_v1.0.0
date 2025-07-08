package com.grepp.teamnotfound.app.controller.api.life_record.payload;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class LifeRecordResponse {

    private Long petId;
    private LocalDate recordAt;

    // 관찰노트
    private NoteResponse note;

    // 수면시간
    private Integer sleepTime;
    // 몸무게
    private Double weight;

    // 산책
    private List<WalkingResponse> walkingList;

    // 식사량
    private List<FeedingResponse> feedingList;

}
