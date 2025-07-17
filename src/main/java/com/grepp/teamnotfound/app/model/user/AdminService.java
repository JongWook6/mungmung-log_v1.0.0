package com.grepp.teamnotfound.app.model.user;

import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.dto.MonthlyArticlesStatsDto;
import com.grepp.teamnotfound.app.model.board.dto.YearlyArticlesStatsDto;
import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import com.grepp.teamnotfound.app.model.user.dto.*;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ArticleService articleService;

    @Transactional(readOnly = true)
    public TotalUsersDto getTotalUsersCount() {
        long totalUsers = userRepository.count();
        return TotalUsersDto.builder()
                .date(OffsetDateTime.now())
                .total(totalUsers)
                .build();
    }

    @Transactional(readOnly = true)
    public List<MonthlyUserStatsDto> getMonthlyUsersStats() {
        OffsetDateTime now = OffsetDateTime.now();

        List<MonthlyUserStatsDto> response = new ArrayList<>();

        // 옛날부터
        for(int i = 4; i>=0; i--){
            OffsetDateTime monthStart = now.minusMonths(i).withDayOfMonth(1);
            OffsetDateTime monthEnd = monthStart.withDayOfMonth(monthStart.toLocalDate().lengthOfMonth());

            int joined = userRepository.countJoinedUsersBetween(monthStart, monthEnd);
            int left = userRepository.countLeftUsersBetween(monthStart, monthEnd);

            MonthlyUserStatsDto stats = MonthlyUserStatsDto.builder()
                    .month(monthStart.getMonthValue())
                    .joinedCount(joined)
                    .leaveCount(left)
                    .build();

            response.add(stats);
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<YearlyUserStatsDto> getYearlyUsersStats() {
        OffsetDateTime now = OffsetDateTime.now();

        List<YearlyUserStatsDto> response = new ArrayList<>();

        for(int i = 4; i>=0; i--){
            OffsetDateTime yearStart = now.minusYears(i).withDayOfYear(1);
            OffsetDateTime yearEnd = yearStart.withDayOfYear(yearStart.toLocalDate().lengthOfYear());

            int joined = userRepository.countJoinedUsersBetween(yearStart, yearEnd);
            int left = userRepository.countLeftUsersBetween(yearStart, yearEnd);

            YearlyUserStatsDto stats = YearlyUserStatsDto.builder()
                    .year(yearStart.getYear())
                    .joinedCount(joined)
                    .leaveCount(left)
                    .build();

            response.add(stats);
        }

        return response;
    }

    public List<MonthlyArticlesStatsDto> getMonthlyArticlesStats() {

        OffsetDateTime now = OffsetDateTime.now();

        List<MonthlyArticlesStatsDto> response = new ArrayList<>();

        for(int i = 4; i>=0; i--){
            OffsetDateTime monthStart = now.minusMonths(i).withDayOfMonth(1);
            OffsetDateTime monthEnd = monthStart.withDayOfMonth(monthStart.toLocalDate().lengthOfMonth());

            int articles = articleService.countArticles(monthStart, monthEnd);

            MonthlyArticlesStatsDto stats = MonthlyArticlesStatsDto.builder()
                    .month(monthStart.getMonthValue())
                    .articlesCount(articles)
                    .build();

            response.add(stats);
        }

        return response;

    }

    public List<YearlyArticlesStatsDto> getYearlyArticlesStats() {
        OffsetDateTime now = OffsetDateTime.now();

        List<YearlyArticlesStatsDto> response = new ArrayList<>();

        for(int i = 4; i>=0; i--){
            OffsetDateTime yearStart = now.minusYears(i).withDayOfYear(1);
            OffsetDateTime yearEnd = yearStart.withDayOfYear(yearStart.toLocalDate().lengthOfYear());

            int articles = articleService.countArticles(yearStart, yearEnd);

            YearlyArticlesStatsDto stats = YearlyArticlesStatsDto.builder()
                    .year(yearStart.getYear())
                    .articlesCount(articles)
                    .build();

            response.add(stats);
        }

        return response;

    }
}
