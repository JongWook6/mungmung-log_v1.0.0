package com.grepp.teamnotfound.app.model.structured_data.repository;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface WalkingRepository extends JpaRepository<Walking, Long> {

    @Query("SELECT w FROM Walking w WHERE w.pet = :pet AND w.recordedAt = :recordedAt AND w.deletedAt IS NULL")
    List<Walking> findWalkingList(Pet pet, LocalDate recordedAt);

}
