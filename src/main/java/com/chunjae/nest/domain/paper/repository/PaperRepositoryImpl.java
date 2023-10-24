package com.chunjae.nest.domain.paper.repository;
import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.paper.entity.QPaper;
import com.chunjae.nest.domain.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PaperRepositoryImpl implements PaperRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QPaper paper = QPaper.paper;

//    @Override
//    public List<Paper> findAll() {
//        return queryFactory
//                .selectFrom(paper)
//                .fetch();
//    }

    @Override
    public List<Paper> searchByWhere(SearchKeywordDTO searchKeywordDTO) {
        return queryFactory
                .selectFrom(paper)
                .where(
                        yearEq(searchKeywordDTO.getYear()),
                        monthEq(searchKeywordDTO.getMonth()),
                        areaAndSubjectEq(searchKeywordDTO.getArea(), searchKeywordDTO.getSubject()),
//                        areaEq(searchKeywordDTO.getArea()),
//                        subjectEq(searchKeywordDTO.getSubject()),
                        keyWordEq(searchKeywordDTO.getSearchOption(), searchKeywordDTO.getSearchKeyword()),
                        categoryEq(searchKeywordDTO.getCategory()),
                        paperStatusEq(searchKeywordDTO.getPaperStatus()),
                        gradeEq(searchKeywordDTO.getGrade())
                )
                .orderBy(paper.id.desc())
                .fetch();
    }

    private BooleanExpression yearEq(String year) {
        System.out.println("레파지토리 부분 - 받은 year 값 : " + year);

        if ("년도".equals(year)) {
            return null;
        } else if ("2023".equals(year)) {
            return paper.year.eq(2023);
        } else if ("2022".equals(year)) {
            return paper.year.eq(2022);
        } else {
            return null;
        }
    }

    private Predicate monthEq(int month) {
        System.out.println("레파지토리 부분 - 받은 month 값 : " + month);
        BooleanExpression monthCondition = (month != 0) ? paper.month.eq(month) : null;

        if (month == 1) {
            monthCondition = monthCondition.or(paper.month.eq(1));
        } else if (month == 2) {
            monthCondition = monthCondition.or(paper.month.eq(2));
        } else if (month == 3) {
            monthCondition = monthCondition.or(paper.month.eq(3));
        } else if (month == 4) {
            monthCondition = monthCondition.or(paper.month.eq(4));
        } else if (month == 5) {
            monthCondition = monthCondition.or(paper.month.eq(5));
        } else if (month == 6) {
            monthCondition = monthCondition.or(paper.month.eq(6));
        } else if (month == 7) {
            monthCondition = monthCondition.or(paper.month.eq(7));
        } else if (month == 8) {
            monthCondition = monthCondition.or(paper.month.eq(8));
        } else if (month == 9) {
            monthCondition = monthCondition.or(paper.month.eq(9));
        } else if (month == 10) {
            monthCondition = monthCondition.or(paper.month.eq(10));
        } else if (month == 11) {
            monthCondition = monthCondition.or(paper.month.eq(11));
        } else if (month == 12) {
            monthCondition = monthCondition.or(paper.month.eq(12));
        }
        return monthCondition;
    }

    private Predicate areaAndSubjectEq(String area, String subject) {
        System.out.println("레파지토리 부분 - 받은 area 값 : " + area);
        System.out.println("레파지토리 부분 - 받은 subject 값 : " + subject);

        BooleanBuilder builder = new BooleanBuilder();

        // "area"와 "subject"의 매핑을 정의
        Map<String, String> areaSubjectMapping = new HashMap<>();
        areaSubjectMapping.put("국어", "화법과 작문");
        areaSubjectMapping.put("국어", "언어와 매체");
        areaSubjectMapping.put("수학", "확률과 통계");
        areaSubjectMapping.put("수학", "미적분");
        areaSubjectMapping.put("수학", "기하");
        areaSubjectMapping.put("영어", "영어");
        areaSubjectMapping.put("한국사", "한국사");
        areaSubjectMapping.put("사회탐구", "생활과 윤리");

        String subjectForMapping = areaSubjectMapping.get(area);

        if (subjectForMapping != null && ("전체".equals(subject) || subject == null)) {
            // "subject"가 "전체"이거나 null인 경우 해당 "area"에 대한 전체 항목을 조회
            builder.and(paper.area.eq(area));
        } else if (subjectForMapping != null && subjectForMapping.equals(subject)) {
            // "area"와 "subject"가 모두 매핑되는 경우 해당 항목을 조회
            builder.and(paper.area.eq(area).and(paper.subject.eq(subject)));
        }

        return builder;
    }

//    private Predicate areaEq(String area) {
//        System.out.println("레파지토리 부분 - 받은 area 값 : " + area);
//
//        if (area == "전체") {
//            return null;
//        } else if (area == "국어") {
//            return paper.area.eq("국어");
//        } else if (area == "수학") {
//            return paper.area.eq("수학");
//        } else if (area == "영어") {
//            return paper.area.eq("영어");
//        } else if (area == "한국사") {
//            return paper.area.eq("한국사");
//        } else if (area == "사회탐구") {
//            return paper.area.eq("사회탐구");
//        } else if (area == "과학탐구") {
//            return paper.area.eq("과학탐구");
//        } else if (area == "직업탐구") {
//            return paper.area.eq("직업탐구");
//        } else if (area == "제2외국어/한문") {
//            return paper.area.eq("제2외국어/한문");
//        }
//        return null;
//    }
//
//    private Predicate subjectEq(String subject) {
//        System.out.println("레파지토리 부분 - 받은 subject 값 : " + subject);
////        BooleanExpression subjectCondition = (subject != null) ? paper.subject.eq(subject) : null;
////
////        return subjectCondition;
//
//        if (subject == null || "전체".equals(subject)) {
//            return null;
//        } else {
//            return paper.subject.eq(subject);
//        }
//    }

    private Predicate keyWordEq(String searchOption, String searchKeyword) {
        System.out.println("레파지토리 부분 - 받은 keyword 값 : " + searchOption + ", " + searchKeyword);

        if (searchOption == "all"){
            return null;
        } else if (searchOption == "user_id"){
            return paper.user.userId.eq(searchKeyword);
        } else if (searchOption == "name") {
            return paper.name.eq(searchKeyword);
        } else {
            // 두 값 모두 넘어오지 않았을 경우
            return null;
        }
    }

    private Predicate categoryEq(String category) {
        System.out.println("레파지토리 부분 - 받은 category 값 : " + category);
        BooleanExpression categoryCondition = (category != null) ? paper.category.eq(category) : null;

        return categoryCondition;
    }

    private BooleanExpression gradeEq(String grade) {
        System.out.println("레파지토리 부분 - 받은 grade 값 : " + grade);

        if ("전체".equals(grade)) {
            return null;
        } else if ("1".equals(grade)) {
            return paper.grade.eq(1);
        } else if ("2".equals(grade)) {
            return paper.grade.eq(2);
        } else if ("3".equals(grade)) {
            return paper.grade.eq(3);
        } else {
            // 두 값 모두 넘어오지 않았을 경우
            return null;
        }
    }

    private BooleanExpression paperStatusEq(String paperStatus) {

        System.out.println("레파지토리 부분 - 받은 paperStatus 값 : " + paperStatus);

        if (paperStatus == "ALL") {
            return null;
        } else if (paperStatus == "DONE") {
            return paper.paperStatus.eq(PaperStatus.valueOf("DONE"));
        } else if (paperStatus == "IN_PROGRESS") {
            return paper.paperStatus.eq(PaperStatus.valueOf("IN_PROGRESS"));
        } else {
            // 두 값 모두 넘어오지 않았을 경우
            return null;
        }
    }
}
