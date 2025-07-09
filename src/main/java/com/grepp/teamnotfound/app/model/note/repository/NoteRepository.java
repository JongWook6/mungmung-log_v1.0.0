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

    Note findFirstByPet(Pet pet);

    Optional<Note> findByPetAndRecordedAt(Pet pet, LocalDate recordedAt);
}
