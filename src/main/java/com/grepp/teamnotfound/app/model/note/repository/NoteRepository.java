package com.grepp.teamnotfound.app.model.note.repository;

import com.grepp.teamnotfound.app.model.note.entity.Note;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Note n WHERE n.pet.petId = :petId AND n.recordedAt = :date")
    boolean existsByPetIdAndRecordedAt(@Param("petId") Long petId, @Param("date") LocalDate date);

    @Query("SELECT n FROM Note n WHERE n.pet = :pet AND n.recordedAt = :recordedAt AND n.deletedAt IS NULL")
    Optional<Note> findNote(Pet pet, LocalDate recordedAt);

}
