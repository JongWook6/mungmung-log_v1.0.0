package com.grepp.teamnotfound.app.controller.api.admin.code;

public enum AdminListSortDirection {
    ASC, DESC;

    public boolean isAsc() {
        return this == ASC;
    }
}
