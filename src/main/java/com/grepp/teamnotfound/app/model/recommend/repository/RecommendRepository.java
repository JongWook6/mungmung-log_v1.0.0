package com.grepp.teamnotfound.app.model.recommend.repository;

import com.grepp.teamnotfound.app.model.pet.code.PetPhase;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.code.RecommendState;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("SELECT r FROM Recommend r WHERE r.deletedAt IS NULL")
    Optional<Recommend> findByRecId(Long recId);

    // 종, 나이, 반려견 상태값이 모두 일치하는 Recommend가 있는지 확인
    @Query("SELECT r FROM Recommend r WHERE r.breed = :breed AND r.age = :age "
            + "AND r.weightState = :weightState "
            + "AND r.walkingState = :walkingState "
            + "AND r.sleepingState = :sleepingState")
    Optional<Recommend> findRecommendByAllStates(
            @Param("breed") PetType breed,
            @Param("age") PetPhase age,
            @Param("weightState") RecommendState weightState,
            @Param("walkingState") RecommendState walkingState,
            @Param("sleepingState") RecommendState sleepingState
    );

}
