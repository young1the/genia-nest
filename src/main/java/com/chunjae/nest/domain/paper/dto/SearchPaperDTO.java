package com.chunjae.nest.domain.paper.dto;

import com.chunjae.nest.domain.paper.entity.PaperStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchPaperDTO {

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
    // 등록자
    // 등록일
}
