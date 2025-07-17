package com.grepp.teamnotfound.app.controller.api.admin;

import com.grepp.teamnotfound.app.controller.api.admin.code.StatsUnit;
import com.grepp.teamnotfound.app.controller.api.admin.payload.ArticlesStatsResponse;
import com.grepp.teamnotfound.app.controller.api.admin.payload.ReportDetailResponse;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UserCountResponse;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UserStatsResponse;
import com.grepp.teamnotfound.app.model.board.dto.MonthlyArticlesStatsDto;
import com.grepp.teamnotfound.app.model.board.dto.YearlyArticlesStatsDto;
import com.grepp.teamnotfound.app.model.report.ReportService;
import com.grepp.teamnotfound.app.model.report.dto.ReportDetailDto;
import com.grepp.teamnotfound.app.model.user.AdminService;
import com.grepp.teamnotfound.app.model.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
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
    private final ReportService reportService;

    @Operation(summary = "전체 가입자 수 조회")
    @GetMapping("v1/stats/users")
    public ResponseEntity<UserCountResponse> users(){
        TotalUsersDto totalUsersDto = adminService.getTotalUsersCount();
        UserCountResponse response = UserCountResponse.builder()
                .date(OffsetDateTime.now())
                .total(totalUsersDto.getTotal())
                .build();
        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "회원 목록 조회")
//    @GetMapping("v1/users")
//    public ResponseEntity<List<UsersListResponse>> usersList(
//            @Valid @ModelAttribute UsersListRequest request
//            ){
//        List<UsersListResponse> responseList = adminService.getUsersList(request);
//        return ResponseEntity.ok(responseList);
//    }

    @Operation(summary = "가입/탈퇴자 수 추이 조회")
    @GetMapping("v1/stats/transition")
    public ResponseEntity<UserStatsResponse> userStatsList(
            @RequestParam(defaultValue = "MONTH") StatsUnit unit
    ) {

        if (unit == StatsUnit.MONTH) {
            List<MonthlyUserStatsDto> monthlyStats = adminService.getMonthlyUsersStats();
            return ResponseEntity.ok(
                    UserStatsResponse.<MonthlyUserStatsDto>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(monthlyStats)
                            .build());
        }
        else {
            List<YearlyUserStatsDto> yearlyStats = adminService.getYearlyUsersStats();
            return ResponseEntity.ok(
                    UserStatsResponse.<YearlyUserStatsDto>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(yearlyStats)
                            .build());
        }
    }

    @Operation(summary = "게시글 수 추이 조회")
    @GetMapping("v1/stats/articles")
    public ResponseEntity<ArticlesStatsResponse> articlesStatsList(
            @RequestParam(defaultValue = "MONTH") StatsUnit unit
    ) {

        if (unit == StatsUnit.MONTH) {
            List<MonthlyArticlesStatsDto> monthlyStats = adminService.getMonthlyArticlesStats();
            return ResponseEntity.ok(
                    ArticlesStatsResponse.<MonthlyArticlesStatsDto>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(monthlyStats)
                            .build());
        }
        else {
            List<YearlyArticlesStatsDto> yearlyStats = adminService.getYearlyArticlesStats();
            return ResponseEntity.ok(
                    ArticlesStatsResponse.<YearlyArticlesStatsDto>builder()
                            .viewDat(OffsetDateTime.now())
                            .stats(yearlyStats)
                            .build());
        }
    }

    @Operation(summary = "신고내역 상세 보기")
    @GetMapping("v1/reports/{reportId}")
    public ResponseEntity<ReportDetailResponse> getReportDetail(@PathVariable Long reportId) {

        ReportDetailDto dto = reportService.getReportDetail(reportId);

        return ResponseEntity.ok(
                ReportDetailResponse.from(dto));
    }

}
