package com.chunjae.nest.domain.paper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchKeywordDTO { // 검색 조건 DTO

    private String year; // 출제 년도
    private String category; // 출제 타입
    private int month; // 출제 월
    private String grade; // 학년
    private String area; // 영역
    private String subject; // 과목
    private String searchOption;  // 검색 옵션 (시험지 명, 등록자)
    private String searchKeyword; // 검색 키워드
    private String paperStatus; // 업로드 상태

    @Override
    public String toString() {
        return "SearchKeywordDTO{" +
                "year=" + year +
                ", category='" + category + '\'' +
                ", month=" + month +
                ", grade=" + grade +
                ", area='" + area + '\'' +
                ", subject='" + subject + '\'' +
                ", searchOption='" + searchOption + '\'' +
                ", searchKeyword='" + searchKeyword + '\'' +
                ", paperStatus='" + paperStatus + '\'' +
                '}';
    }

    public boolean isEmpty() {
        return year == null && month == 0 && area == null && subject == null
                && searchOption == null && searchKeyword == null && category == null
                && paperStatus == null && grade == null;
    }
}
