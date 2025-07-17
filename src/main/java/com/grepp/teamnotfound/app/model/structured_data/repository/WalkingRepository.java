package com.grepp.teamnotfound.app.model.structured_data.repository;

import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface WalkingRepository extends JpaRepository<Walking, Long> {

    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query("UPDATE Walking w SET w.deletedAt = CURRENT_TIMESTAMP WHERE w.lifeRecord.lifeRecordId = :lifeRecordId AND w.deletedAt IS NULL")
    void delete(@Param("lifeRecordId") Long lifeRecordId);

    Optional<List<Walking>> findAllByPetAndRecordedAtBetween(Pet pet, LocalDate localDate, LocalDate date);
}
