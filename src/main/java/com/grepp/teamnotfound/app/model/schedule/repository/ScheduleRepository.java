package com.grepp.teamnotfound.app.model.schedule.repository;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByPetAndScheduleDateBetweenAndDeletedAtNull(Pet pet, LocalDate start, LocalDate end);
}
