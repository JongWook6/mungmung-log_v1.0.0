package com.grepp.teamnotfound.app.model.report;

import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import com.grepp.teamnotfound.app.model.report.dto.ReportDetailDto;
import com.grepp.teamnotfound.app.model.report.entity.Report;
import com.grepp.teamnotfound.app.model.report.repository.ReportRepository;
import com.grepp.teamnotfound.app.model.user.UserService;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.ReportErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    private final UserService userService;
    private final ArticleService articleService;

    @Transactional(readOnly = true)
    public ReportDetailDto getReportDetail(Long reportId) {
        Report report = reportRepository.findByReportId(reportId)
                .orElseThrow(() -> new BusinessException(ReportErrorCode.REPORT_NOT_FOUND));

        String reporterNickname = userService.getRequiredUserNickname(report.getReported().getUserId());

        Long articleId = (report.getType()==ReportType.REPLY) ?
                articleService.getRequiredArticleIdByReplyId(report.getContentId())
                : report.getContentId();

        return ReportDetailDto.from(report, reporterNickname, articleId);
    }
}
