package com.chunjae.nest.domain.paper.entity;

import com.chunjae.nest.common.BaseEntity;
import com.chunjae.nest.domain.paper.dto.req.PaperRequest;
import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
public class Paper extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int grade;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int totalCount;

    @Column(length = 5, nullable = false)
    private String category;

    @Column(length = 10, nullable = false)
    private String area;

    @Column(length = 30, nullable = false)
    private String subject;

    @Column(nullable = false)
    private int ocrCount;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaperStatus paperStatus;

    @OneToMany(mappedBy = "paper")
    private List<PaperAssignment> paperAssignments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paper")
    private PaperFile paperFile;

    @OneToMany(mappedBy = "paper")
    private List<Question> questions = new ArrayList<>();

    public void paperToUpdate(PaperRequest paperRequest) {
        this.year = paperRequest.getYear();
        this.month = paperRequest.getMonth();
        this.grade = paperRequest.getGrade();
        this.name = paperRequest.getName();
        this.totalCount = paperRequest.getTotalCount();
        this.category = paperRequest.getCategory();
        this.area = paperRequest.getArea();
        this.subject = paperRequest.getSubject();
    }

    public void updatePaperStatus(PaperStatus paperStatus) {
        this.paperStatus = paperStatus;
    }

    public void setOcrCount(int ocrCount) {
        this.ocrCount = ocrCount;
    }
}
