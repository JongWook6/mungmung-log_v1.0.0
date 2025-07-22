package com.grepp.teamnotfound.app.model.recommend;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import com.grepp.teamnotfound.app.model.recommend.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;

    public Recommend matchDailyRecommend(Pet pet, LocalDate date) {
        return null;
    }

    @Transactional(readOnly = true)
    public String getRecommend(Pet pet) {
    }
}
