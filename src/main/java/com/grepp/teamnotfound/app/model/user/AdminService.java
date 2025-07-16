package com.grepp.teamnotfound.app.model.user;

import com.grepp.teamnotfound.app.model.board.repository.ArticleRepository;
import com.grepp.teamnotfound.app.model.user.dto.*;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UsersListRequest;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UsersListResponse;
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
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public TotalUsersCount getTotalUsersCount() {
        long totalUsers = userRepository.count();
        return TotalUsersCount.builder()
                .date(OffsetDateTime.now())
                .total(totalUsers)
                .build();
    }

    public List<UsersListResponse> getUsersList(UsersListRequest request) {
        return null;
    }


    @Transactional(readOnly = true)
    public List<MonthlyUserStats> getMonthlyUsersStats() {
        OffsetDateTime now = OffsetDateTime.now();

        List<MonthlyUserStats> response = new ArrayList<>();

        // 옛날부터
        for(int i = 4; i>=0; i--){
            OffsetDateTime monthStart = now.minusMonths(i).withDayOfMonth(1);
            OffsetDateTime monthEnd = monthStart.withDayOfMonth(monthStart.toLocalDate().lengthOfMonth());

            int joined = userRepository.countJoinedUsersBetween(monthStart, monthEnd);
            int left = userRepository.countLeftUsersBetween(monthStart, monthEnd);

            MonthlyUserStats stats = MonthlyUserStats.builder()
                    .month(monthStart.getMonthValue())
                    .joinedCount(joined)
                    .leaveCount(left)
                    .build();

            response.add(stats);
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<YearlyUserStats> getYearlyUsersStats() {
        OffsetDateTime now = OffsetDateTime.now();

        List<YearlyUserStats> response = new ArrayList<>();

        for(int i = 4; i>=0; i--){
            OffsetDateTime yearStart = now.minusYears(i).withDayOfYear(1);
            OffsetDateTime yearEnd = yearStart.withDayOfYear(yearStart.toLocalDate().lengthOfYear());

            int joined = userRepository.countJoinedUsersBetween(yearStart, yearEnd);
            int left = userRepository.countLeftUsersBetween(yearStart, yearEnd);

            YearlyUserStats stats = YearlyUserStats.builder()
                    .year(yearStart.getYear())
                    .joinedCount(joined)
                    .leaveCount(left)
                    .build();

            response.add(stats);
        }

        return response;
    }

    public List<MonthlyArticlesStats> getMonthlyArticlesStats() {

        OffsetDateTime now = OffsetDateTime.now();

        List<MonthlyArticlesStats> response = new ArrayList<>();

        for(int i = 4; i>=0; i--){
            OffsetDateTime monthStart = now.minusMonths(i).withDayOfMonth(1);
            OffsetDateTime monthEnd = monthStart.withDayOfMonth(monthStart.toLocalDate().lengthOfMonth());

            int articles = articleRepository.countArticlesBetween(monthStart, monthEnd);

            MonthlyArticlesStats stats = MonthlyArticlesStats.builder()
                    .month(monthStart.getMonthValue())
                    .articlesCount(articles)
                    .build();

            response.add(stats);
        }

        return response;

    }

    public List<YearlyArticlesStats> getYearlyArticlesStats() {
        OffsetDateTime now = OffsetDateTime.now();

        List<YearlyArticlesStats> response = new ArrayList<>();

        for(int i = 4; i>=0; i--){
            OffsetDateTime yearStart = now.minusYears(i).withDayOfYear(1);
            OffsetDateTime yearEnd = yearStart.withDayOfYear(yearStart.toLocalDate().lengthOfYear());

            int articles = articleRepository.countArticlesBetween(yearStart, yearEnd);

            YearlyArticlesStats stats = YearlyArticlesStats.builder()
                    .year(yearStart.getYear())
                    .articlesCount(articles)
                    .build();

            response.add(stats);
        }

        return response;

    }
}
