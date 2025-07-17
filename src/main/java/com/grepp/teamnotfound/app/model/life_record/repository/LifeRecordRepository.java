package com.grepp.teamnotfound.app.model.life_record.repository;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeRecordRepository extends JpaRepository<LifeRecord, Long> {

    @Query("SELECT l FROM LifeRecord l WHERE l.lifeRecordId = :lifeRecordId AND l.deletedAt IS NULL")
    Optional<LifeRecord> findByLifeRecordId(@Param("lifeRecordId") Long lifeRecordId);

    @Query("SELECT l.lifeRecordId FROM LifeRecord l WHERE l.pet.petId = :petId AND l.recordedAt = :recordAt AND l.deletedAt IS NULL")
    Optional<Long> findLifeRecordId(@Param("petId") Long petId, @Param("recordAt") LocalDate recordAt);

}
