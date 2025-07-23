package com.grepp.teamnotfound.app.model.recommend;

import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.recommend.entity.DailyRecommend;
import com.grepp.teamnotfound.app.model.recommend.entity.Recommend;
import com.grepp.teamnotfound.app.model.recommend.repository.DailyRecommendRepository;
import com.grepp.teamnotfound.infra.error.exception.RecommendException;
import com.grepp.teamnotfound.infra.error.exception.code.RecommendErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public Boolean existsByPetAndDate(Pet pet) {
        return dailyRecommendRepository.existsByPetAndDate(pet, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public String getRecommendByPet(Pet pet) {
        Recommend recommend = dailyRecommendRepository.findRecIdByPetAndDate(pet, LocalDate.now())
                .orElseThrow(() -> new RecommendException(RecommendErrorCode.RECOMMEND_NOT_FOUND));

        return recommend.getContent();
    }

}
