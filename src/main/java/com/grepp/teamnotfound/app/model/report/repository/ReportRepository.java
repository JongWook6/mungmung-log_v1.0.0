package com.grepp.teamnotfound.app.model.report.repository;

import com.grepp.teamnotfound.app.model.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByReportId(Long reportId);
}
