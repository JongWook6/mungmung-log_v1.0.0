package com.grepp.teamnotfound.app.model.notification.entity;

import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.infra.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "ScheduleNoti")
@Getter
@Setter
public class ServiceNoti extends BaseEntity {

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
    private Long serviceNotiId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotiType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
