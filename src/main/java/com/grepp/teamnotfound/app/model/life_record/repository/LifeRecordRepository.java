package com.grepp.teamnotfound.app.model.life_record.repository;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeRecordRepository extends JpaRepository<LifeRecord, Long> {

}
