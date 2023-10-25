package com.chunjae.nest.domain.paper.entity;

import com.chunjae.nest.common.BaseEntity;
import com.chunjae.nest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
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
    private short year;

    @Column(nullable = false)
    private short month;

    @Column(nullable = false)
    private short grade;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private short totalCount;

    @Column(length = 5, nullable = false)
    private String category;

    @Column(length = 10, nullable = false)
    private String area;

    @Column(length = 30, nullable = false)
    private String subject;

    @Column(nullable = false)
    private short ocrCount;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaperStatus paperStatus;

    @OneToMany(mappedBy = "paper")
    private List<PaperAssignment> paperAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "paper")
    private List<PaperFile> paperFiles = new ArrayList<>();
}
