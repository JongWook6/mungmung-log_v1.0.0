package com.grepp.teamnotfound.app.model.notification.repository;

import com.grepp.teamnotfound.app.model.notification.entity.ScheduleNoti;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleNotiRepository extends JpaRepository<ScheduleNoti, Long> {

    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query("UPDATE ScheduleNoti s SET s.deletedAt = CURRENT_TIMESTAMP WHERE s.user = :userId")
    void deleteAllByUser(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE ScheduleNoti s SET s.deletedAt = CURRENT_TIMESTAMP WHERE s.scheduleNotiId = :notiId")
    void deleteOne(@Param("notiId") Long notiId);
}
