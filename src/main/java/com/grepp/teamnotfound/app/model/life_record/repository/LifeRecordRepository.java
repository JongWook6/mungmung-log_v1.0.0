package com.grepp.teamnotfound.app.model.life_record.repository;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.code.FeedUnit;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeRecordRepository extends JpaRepository<LifeRecord, Long>, LifeRecordRepositoryCustom {

    @Query("SELECT l FROM LifeRecord l WHERE l.lifeRecordId = :lifeRecordId AND l.deletedAt IS NULL")
    Optional<LifeRecord> findByLifeRecordId(@Param("lifeRecordId") Long lifeRecordId);

    @Query("SELECT l.lifeRecordId FROM LifeRecord l WHERE l.pet.petId = :petId AND l.recordedAt = :recordAt AND l.deletedAt IS NULL")
    Optional<Long> findLifeRecordId(@Param("petId") Long petId, @Param("recordAt") LocalDate recordAt);

    @Query("SELECT l FROM LifeRecord l WHERE l.pet = :pet AND l.deletedAt IS NULL ORDER BY l.recordedAt DESC LIMIT 10")
    List<LifeRecord> findTop10ByPet(Pet pet);

    List<LifeRecord> findTop10ByPetAndDeletedAtNullAndRecordedAtBeforeAndSleepingTimeIsNotNullOrderByRecordedAtDesc(Pet pet, LocalDate date);

    List<LifeRecord> findTop10ByPetAndDeletedAtNullAndRecordedAtBeforeAndWeightIsNotNullOrderByRecordedAtDesc(Pet pet, LocalDate date);

    List<LifeRecord> findByPetAndDeletedAtNullAndRecordedAtBetweenOrderByRecordedAtDesc(Pet pet, LocalDate date, LocalDate localDate);

    @Query("SELECT l.content FROM LifeRecord l WHERE l.pet = :pet AND l.deletedAt IS NULL AND l.recordedAt BETWEEN :startDate AND :endDate ORDER BY l.recordedAt DESC")
    List<String> findWeekNote(@Param("pet") Pet pet, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT l FROM LifeRecord l WHERE l.recordedAt = :today AND l.deletedAt IS NULL")
    List<LifeRecord> findToday(LocalDate today);

    @Query("SELECT f.unit FROM Feeding f JOIN f.lifeRecord lr WHERE lr.pet.petId = :petId ORDER BY f.mealTime DESC LIMIT 1")
    Optional<FeedUnit> findRecentFeedUnit(@Param("petId") Long petId);

}
