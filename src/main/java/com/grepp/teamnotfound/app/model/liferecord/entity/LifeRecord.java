package com.grepp.teamnotfound.app.model.liferecord.entity;

import com.grepp.teamnotfound.app.model.liferecord.dto.LifeRecordDto;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import com.grepp.teamnotfound.app.model.structured_data.entity.Walking;
import com.grepp.teamnotfound.infra.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;


@Entity
@Table(name = "LifeRecords")
@Getter
@Setter
public class LifeRecord extends BaseEntity {

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
    private Long lifeRecordId;

    @Column(nullable = false)
    private LocalDate recordedAt;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    private Double weight;

    private Integer sleepingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @OneToMany(mappedBy = "lifeRecord", cascade = CascadeType.ALL)
    @Where(clause = "deleted_at IS NULL")
    private List<Walking> walkingList = new ArrayList<>();

    @OneToMany(mappedBy = "lifeRecord", cascade = CascadeType.ALL)
    @Where(clause = "deleted_at IS NULL")
    private List<Feeding> feedingList = new ArrayList<>();

    public void addWalking(Walking walking) {
        this.walkingList.add(walking);
        walking.setLifeRecord(this);
    }

    public void addFeeding(Feeding feeding) {
        this.feedingList.add(feeding);
        feeding.setLifeRecord(this);
    }

    public static LifeRecord create(Pet pet, LifeRecordDto dto) {
        LifeRecord lifeRecord = new LifeRecord();
        lifeRecord.setPet(pet);
        lifeRecord.setRecordedAt(dto.getRecordAt());
        lifeRecord.setContent(dto.getContent());
        lifeRecord.setWeight(dto.getWeight());
        lifeRecord.setSleepingTime(dto.getSleepTime());

        dto.getWalkingList().stream()
                .map(Walking::from)
                .forEach(lifeRecord::addWalking);

        dto.getFeedingList().stream()
                .map(Feeding::from)
                .forEach(lifeRecord::addFeeding);

        return lifeRecord;
    }

}
