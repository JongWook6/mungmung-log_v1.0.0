package com.grepp.teamnotfound.app.model.vaccination.entity;

import com.grepp.teamnotfound.app.model.vaccination.code.VaccineType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Vaccines")
@Getter
@Setter
public class Vaccine {

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
    private Long vaccineId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private VaccineType vaccination;

    @Column(nullable = false)
    private Integer period;

    @Column(nullable = false)
    private Integer boosterCycle;

    @Column(nullable = false)
    private Integer boosterCount;

    @Column(nullable = false)
    private Integer additionalCycle;

}
