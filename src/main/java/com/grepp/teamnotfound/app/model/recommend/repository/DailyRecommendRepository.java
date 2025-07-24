package com.grepp.teamnotfound.app.model.recommend.repository;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.DailyRecommend;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyRecommendRepository extends JpaRepository<DailyRecommend, Long> {

    Optional<DailyRecommend> findByPetAndDate(Pet pet, LocalDate date);

    Boolean existsByPetAndDate(Pet pet, LocalDate now);

    Optional<Recommend> findRecommendByPetAndDate(Pet pet, LocalDate now);
}
