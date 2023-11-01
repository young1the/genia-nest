package com.chunjae.nest.domain.question.entity;

import com.chunjae.nest.common.BaseEntity;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int num;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private QuestionStatus questionStatus;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "question")
    private QuestionFile questionFile;

    public void updateQuestionContent(String content) {
        this.content = content;
    }

    public void updateType(QuestionType type){
        this.type = type;
    }

    public void updateQuestionStatus(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }
}
