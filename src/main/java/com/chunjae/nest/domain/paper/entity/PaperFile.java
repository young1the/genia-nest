package com.chunjae.nest.domain.paper.entity;

import com.chunjae.nest.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PaperFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id")
    private Paper paper;


    @Builder
    public PaperFile(String name, String url, Paper paper) {
        this.name = name;
        this.url = url;
        this.paper = paper;
    }
}
