package com.grepp.teamnotfound.app.model.report.repository;

import com.grepp.teamnotfound.app.model.report.code.ReportType;
import com.grepp.teamnotfound.app.model.report.entity.Report;
import com.grepp.teamnotfound.app.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByReportId(Long reportId);

    @Query("select count(r) > 0 " +
            "from Report r " +
            "where r.reporter = :reporter " +
            "and r.type = :type " +
            "and r.contentId = :contentId")
    boolean duplicateReport(@Param("reporter") User reporter, @Param("type") ReportType reportType, @Param("contentId") Long contentId);
}
