package com.grepp.teamnotfound.app.model.recommend.repository;

import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

}
