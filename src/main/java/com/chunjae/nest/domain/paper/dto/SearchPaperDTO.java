package com.chunjae.nest.domain.paper.dto;

import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 대신하는 어노테이션
public class SearchPaperDTO { // 검색시 나와야하는 리스트 DTO

    private Long id; // 시험지 번호
    private short year; // 출제 년도
    private String category; // 출제 타입
    private short month; // 출제 월
    private short grade; // 학년
    private String area; // 영역
    private String subject; // 과목
    private String name; // 시험지 명
    private PaperStatus paperStatus; // 업로드 상태
    private int totalCount; // 총 건수
    private User user_id; // 등록자
    private LocalDateTime createdAt; // 등록일

    @QueryProjection // Q-Type을 만들기위한 어노테이션
    public SearchPaperDTO(Long id, short year, String category, short month, short grade, String area, String subject, String name, PaperStatus paperStatus, int totalCount, User user_id, LocalDateTime createdAt) {
        this.id = id;
        this.year = year;
        this.category = category;
        this.month = month;
        this.grade = grade;
        this.area = area;
        this.subject = subject;
        this.name = name;
        this.paperStatus = paperStatus;
        this.totalCount = totalCount;
        this.user_id = user_id;
        this.createdAt = createdAt;
    }

}
