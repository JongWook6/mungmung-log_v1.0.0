package com.grepp.teamnotfound.app.model.board.code;


import org.springframework.data.domain.Sort;

public enum SortType {
    DATE, LIKE, VIEW;

    public Sort toSort() {
        return switch (this) {
            case DATE -> Sort.by("createdAt").descending();
            case LIKE -> Sort.by("likes").descending();
            case VIEW -> Sort.by("views").descending();
        };
    }
}
