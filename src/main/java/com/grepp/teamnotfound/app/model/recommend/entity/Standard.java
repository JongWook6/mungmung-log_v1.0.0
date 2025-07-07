package com.grepp.teamnotfound.app.model.recommend.entity;

import com.grepp.teamnotfound.app.model.pet.code.PetPhase;
import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Standard")
@Getter
@Setter
public class Standard extends BaseEntity {
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
    private Long standardId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PetType breed;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private PetPhase age;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private PetSize size;

    @Column(nullable = false)
    private Float minWeight;

    @Column(nullable = false)
    private Float maxWeight;

    @Column(nullable = false)
    private Integer minWalk;

    @Column(nullable = false)
    private Integer maxWalk;

    @Column(nullable = false)
    private Integer minSleep;

    @Column(nullable = false)
    private Integer maxSleep;
}
