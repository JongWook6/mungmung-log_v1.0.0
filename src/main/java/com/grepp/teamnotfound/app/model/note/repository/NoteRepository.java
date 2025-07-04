package com.grepp.teamnotfound.app.model.note.repository;

import com.grepp.teamnotfound.app.model.note.entity.Note;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Note findFirstByPet(Pet pet);

}
