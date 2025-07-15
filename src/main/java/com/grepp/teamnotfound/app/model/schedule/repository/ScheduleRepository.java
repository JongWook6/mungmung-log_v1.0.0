package com.grepp.teamnotfound.app.model.schedule.repository;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import com.grepp.teamnotfound.app.model.schedule.entity.Schedule;
import com.grepp.teamnotfound.app.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByNameAndCycleAndCycleEnd(String name, ScheduleCycle cycle, LocalDate cycleEnd);

    @Query("""
           select s
           from   Schedule s
           join   fetch s.pet p
           where  s.user         = :user
             and  s.scheduleDate between :start and :end
             and  s.deletedAt    is null
           """)
    List<Schedule> findMonthListByUser(
            @Param("user")  User      user,
            @Param("start") LocalDate start,
            @Param("end")   LocalDate end);
}
