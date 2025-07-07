package com.grepp.teamnotfound.app.model.board.entity;

import com.grepp.teamnotfound.infra.code.ImgType;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ArticleImgs")
@Getter
@Setter
public class ArticleImg extends BaseEntity {

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
    private Long articleImgId;

    @Column(nullable = false)
    private String savePath;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ImgType type;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String renamedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

}