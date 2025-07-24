package com.grepp.teamnotfound.app.model.recommend.repository;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.DailyRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyRecommendRepository extends JpaRepository<DailyRecommend, Long> {

    Optional<DailyRecommend> findByPetAndDate(Pet pet, LocalDate date);
}
