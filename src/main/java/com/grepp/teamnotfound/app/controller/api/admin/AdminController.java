package com.grepp.teamnotfound.app.controller.api.admin;

import com.grepp.teamnotfound.app.controller.api.admin.payload.UserCountResponse;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UsersListRequest;
import com.grepp.teamnotfound.app.controller.api.admin.payload.UsersListResponse;
import com.grepp.teamnotfound.app.model.user.AdminService;
import com.grepp.teamnotfound.infra.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        UserCountResponse response = adminService.getTotalUsersCount();
        return ResponseEntity.ok((ApiResponse.success(response)));
    }

//    @Operation(summary = "가입자 수 추이 조회")
//    @GetMapping("v1/stats/transition")
//    public ResponseEntity<ApiResponse<List<?>>> userStatsList(){
//        List<UsersStatsResponse> response = adminService.getUserStatsList();
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }

    @Operation(summary = "회원 목록 조회")
    @GetMapping("v1/users")
    public ResponseEntity<ApiResponse<List<UsersListResponse>>> usersList(
            @Valid @ModelAttribute UsersListRequest request
            ){
        List<UsersListResponse> responseList = adminService.getUsersList(request);
        return ResponseEntity.ok(ApiResponse.success(responseList));
    }

//    @Operation(summary = "게시글 수 추이 조회")
//    @GetMapping("v1/stats/articles")
//    public ResponseEntity<ApiResponse<List<?>>> userStatsList(){
//        List<ArticlesStatsResponse> response = adminService.getArticleStatsList();
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }


}
