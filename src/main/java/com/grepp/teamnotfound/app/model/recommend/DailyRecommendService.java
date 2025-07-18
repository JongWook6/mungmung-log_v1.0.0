package com.grepp.teamnotfound.app.model.recommend;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.DailyRecommend;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import com.grepp.teamnotfound.app.model.recommend.repository.DailyRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyRecommendService {

    private final DailyRecommendRepository dailyRecommendRepository;
    private final RecommendService recommendService;

    public String getRecommend(Pet pet, LocalDate date) {
        Optional<DailyRecommend> dailyRecommend = dailyRecommendRepository.findByPetAndDate(pet, date);
        if (dailyRecommend.isPresent()){
            return dailyRecommend.get().getRec().getContent();
        }else{
            Recommend recommend = recommendService.matchDailyRecommend(pet, date);
            DailyRecommend matchRecommend = DailyRecommend.builder()
                    .rec(recommend)
                    .date(date)
                    .pet(pet)
                    .build();
            return recommend.getContent();
        }
    }
}
