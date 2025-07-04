package com.grepp.teamnotfound.app.model.notification.entity;

import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import com.grepp.teamnotfound.app.model.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Notifications")
@Getter
@Setter
public class Notification {

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
    private Long notiId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private Boolean isChecked;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotiType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

