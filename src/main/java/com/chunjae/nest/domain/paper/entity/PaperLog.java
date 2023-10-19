package com.chunjae.nest.domain.paper.entity;

import com.chunjae.nest.common.BaseLogEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PaperLog extends BaseLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String url;

    @Builder
    public PaperLog(String url, String userId) {
        super.userId = userId;
        this.url = url;
    }
}
