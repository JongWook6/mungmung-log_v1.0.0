package com.grepp.teamnotfound.app.model.structured_data.repository;

import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FeedingRepository extends JpaRepository<Feeding, Long> {

    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query("UPDATE Feeding f SET f.deletedAt = CURRENT_TIMESTAMP WHERE f.lifeRecord.lifeRecordId = :lifeRecordId AND f.deletedAt IS NULL")
    void delete(@Param("lifeRecordId")  Long lifeRecordId);

}
