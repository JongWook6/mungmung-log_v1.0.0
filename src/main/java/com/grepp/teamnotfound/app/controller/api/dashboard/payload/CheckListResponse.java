package com.grepp.teamnotfound.app.controller.api.dashboard.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CheckListResponse {
    private List<Todo> weightList;
}

@Data
@Builder
class Todo {
    private String name;
    private Boolean isDone;
}
