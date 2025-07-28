package com.grepp.teamnotfound.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class TimeUtils {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public static LocalDateTime toKST(OffsetDateTime time) {
        return time == null ?
                null :
                time.atZoneSameInstant(KST).toLocalDateTime();
    }
}
