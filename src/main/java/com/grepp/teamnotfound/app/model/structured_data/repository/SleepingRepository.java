package com.grepp.teamnotfound.app.model.structured_data.repository;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.entity.Sleeping;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SleepingRepository extends JpaRepository<Sleeping, Long> {

    @Query("SELECT s FROM Sleeping s WHERE s.pet = :pet AND s.recordedAt = :recordedAt AND s.deletedAt IS NULL")
    Optional<Sleeping> findSleeping(Pet pet, LocalDate recordedAt);

}
