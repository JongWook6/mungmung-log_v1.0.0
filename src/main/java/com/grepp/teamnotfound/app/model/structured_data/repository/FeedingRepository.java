package com.grepp.teamnotfound.app.model.structured_data.repository;

import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedingRepository extends JpaRepository<Feeding, Long> {

}
