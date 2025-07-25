package com.grepp.teamnotfound.app.model.recommend.repository;

import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.recommend.entity.Standard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardRepository extends JpaRepository<Standard, Long> {

    // 반려견의 나이에 따른 Breed, Size, Age 기준으로 Standard 데이터 반환
    @Query("SELECT s FROM Standard s WHERE s.breed = :breed AND s.startAge <= :ageInMonths ORDER BY s.startAge DESC")
    Optional<Standard> findStandardByBreedAndAge(@Param("breed") PetType breed, @Param("ageInMonths") Integer ageInMonths);

}
