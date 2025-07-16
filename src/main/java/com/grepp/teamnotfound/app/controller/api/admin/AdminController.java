package com.grepp.teamnotfound.app.controller.api.admin;

import com.grepp.teamnotfound.app.controller.api.admin.code.StatsUnit;
import com.grepp.teamnotfound.app.controller.api.admin.payload.*;
import com.grepp.teamnotfound.app.model.user.AdminService;
import com.grepp.teamnotfound.app.model.user.UserService;
import com.grepp.teamnotfound.app.model.user.dto.*;
import com.grepp.teamnotfound.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "관리자 권한 테스트용")
    @PostMapping("v1/hello")
    public ResponseEntity<String> adminTest(){
        return ResponseEntity.ok("Admin 사용자만 접근 가능");
    }

    @Operation(summary = "전체 가입자 수 조회")
    @GetMapping("v1/stats/users")
    public ResponseEntity<ApiResponse<UserCountResponse>> users(){
        TotalUsersCount totalUsersCount = adminService.getTotalUsersCount();
        UserCountResponse response = UserCountResponse.builder()
                .date(OffsetDateTime.now())
                .total(totalUsersCount.getTotal())
                .build();
        return ResponseEntity.ok((ApiResponse.success(response)));
    }

    @Operation(summary = "회원 목록 조회")
    @GetMapping("v1/users")
    public ResponseEntity<ApiResponse<List<UsersListResponse>>> usersList(
            @Valid @ModelAttribute UsersListRequest request
            ){
        List<UsersListResponse> responseList = adminService.getUsersList(request);
        return ResponseEntity.ok(ApiResponse.success(responseList));
    }

    @Operation(summary = "가입/탈퇴자 수 추이 조회")
    @GetMapping("v1/stats/transition")
    public ResponseEntity<ApiResponse<UserStatsResponse>> userStatsList(
            @RequestParam(defaultValue = "MONTH") StatsUnit unit
    ) {

        if (unit == StatsUnit.MONTH) {
            List<MonthlyUserStats> monthlyStats = adminService.getMonthlyUsersStats();
            return ResponseEntity.ok(ApiResponse.success(
                    UserStatsResponse.<MonthlyUserStats>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(monthlyStats)
                            .build()));
        }
        else {
            List<YearlyUserStats> yearlyStats = adminService.getYearlyUsersStats();
            return ResponseEntity.ok(ApiResponse.success(
                    UserStatsResponse.<YearlyUserStats>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(yearlyStats)
                            .build()));
        }
    }

    @Operation(summary = "게시글 수 추이 조회")
    @GetMapping("v1/stats/articles")
    public ResponseEntity<ApiResponse<ArticlesStatsResponse>> articlesStatsList(
            @RequestParam(defaultValue = "MONTH") StatsUnit unit
    ) {

        if (unit == StatsUnit.MONTH) {
            List<MonthlyArticlesStats> monthlyStats = adminService.getMonthlyArticlesStats();
            return ResponseEntity.ok(ApiResponse.success(
                    ArticlesStatsResponse.<MonthlyArticlesStats>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(monthlyStats)
                            .build()));
        }
        else {
            List<YearlyArticlesStats> yearlyStats = adminService.getYearlyArticlesStats();
            return ResponseEntity.ok(ApiResponse.success(
                    ArticlesStatsResponse.<YearlyArticlesStats>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(yearlyStats)
                            .build()));
        }
    }

}
