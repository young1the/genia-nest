package com.chunjae.nest.domain.question.entity;

import com.chunjae.nest.common.BaseLogEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class QuestionLog extends BaseLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String questionUrl;

    @Column(length = 255, nullable = false)
    private String paperName;

    @Column(nullable = false)
    private int questionNum;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;
}
