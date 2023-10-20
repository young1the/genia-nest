package com.chunjae.nest.domain.paper.entity;

import com.chunjae.nest.common.BaseEntity;
import com.chunjae.nest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Paper extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
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

    @Builder
    public Paper(User user, int year, int month, int grade, String name, int totalCount, String category, String area, String subject, int ocrCount, PaperStatus paperStatus) {
        this.user = user;
        this.year = year;
        this.month = month;
        this.grade = grade;
        this.name = name;
        this.totalCount = totalCount;
        this.category = category;
        this.area = area;
        this.subject = subject;
        this.ocrCount = ocrCount;
        this.paperStatus = paperStatus;
    }


}
