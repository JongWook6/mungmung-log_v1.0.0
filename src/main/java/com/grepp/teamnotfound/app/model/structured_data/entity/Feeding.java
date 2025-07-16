package com.grepp.teamnotfound.app.model.structured_data.entity;

import com.grepp.teamnotfound.app.model.life_record.entity.LifeRecord;
import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import com.grepp.teamnotfound.infra.entity.BaseEntity;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Feedings")
@Getter
@Setter
public class Feeding extends BaseEntity {

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
    private Long feedingId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private OffsetDateTime mealTime;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private FeedUnit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_record_id", nullable = false)
    private LifeRecord lifeRecord;

}
