package com.grepp.teamnotfound.app.model.structured_data.repository;

import com.grepp.teamnotfound.app.model.structured_data.entity.Sleeping;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SleepingRepository extends JpaRepository<Sleeping, Long> {

//    Sleeping findFirstByPet(Pet pet);

}
