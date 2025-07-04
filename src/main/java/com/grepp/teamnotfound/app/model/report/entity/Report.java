package com.grepp.teamnotfound.app.model.report.entity;

import com.grepp.teamnotfound.app.model.report.code.ReportCategory;
import com.grepp.teamnotfound.app.model.report.code.ReportType;
import com.grepp.teamnotfound.app.model.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Reports")
@Getter
@Setter
public class Report {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "primary_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "primary_sequence"
    )
    private Long reportId;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ReportCategory category;

    @Column(nullable = false, columnDefinition = "text")
    private String reason;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id", nullable = false)
    private User reported;

    @OneToOne(mappedBy = "report", fetch = FetchType.LAZY)
    private ReportResult result;

}

