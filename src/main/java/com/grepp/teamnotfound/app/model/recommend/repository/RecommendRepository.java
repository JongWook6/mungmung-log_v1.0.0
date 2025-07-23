package com.grepp.teamnotfound.app.model.recommend.repository;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("SELECT r FROM Recommend r WHERE r.deletedAt IS NULL")
    Optional<Recommend> findByRecId(Long recId);

    Boolean existsByPetAndDate(Pet pet, LocalDate now);

    Optional<Recommend> findByPetAndDate(Pet pet, LocalDate now);

}
