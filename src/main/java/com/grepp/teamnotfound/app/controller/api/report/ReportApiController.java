package com.grepp.teamnotfound.app.controller.api.report;

import com.grepp.teamnotfound.app.controller.api.report.payload.ReportRequest;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/reports")
public class ReportApiController {

    @PostMapping
    @Operation(summary = "커뮤니티 게시글/댓글 신고")
    public ResponseEntity<?> createReport(
        @ModelAttribute ReportRequest request
    ) {
        return ResponseEntity.ok(Map.of("data", Map.of("msg", "신고가 정상적으로 접수되었습니다.")));
    }

}
