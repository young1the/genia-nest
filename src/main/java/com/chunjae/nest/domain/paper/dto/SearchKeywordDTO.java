package com.chunjae.nest.domain.paper.dto;

import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchKeywordDTO { // 검색 조건 DTO

    private short year; // 출제 년도
    private String category; // 출제 타입
    private short month; // 출제 월
    private short grade; // 학년
    private String area; // 영역
    private String subject; // 과목
    private String name; // 시험지 명
    private User user_id; // 등록자
    private PaperStatus paperStatus; // 업로드 상태
}
