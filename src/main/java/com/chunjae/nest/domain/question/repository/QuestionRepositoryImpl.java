package com.chunjae.nest.domain.question.repository;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.paper.entity.QPaper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    QPaper paper = QPaper.paper;

    @Override
    public Page<Paper> searchOCR(SearchKeywordDTO searchKeywordDTO, Pageable pageable) {
        JPAQuery<Paper> query = queryFactory
                .selectFrom(paper)
                .where(
                        yearEq(searchKeywordDTO.getYear()),
                        monthEq(searchKeywordDTO.getMonth()),
                        areaAndSubjectEq(searchKeywordDTO.getArea(), searchKeywordDTO.getSubject()),
                        keyWordEq(searchKeywordDTO.getSearchOption(), searchKeywordDTO.getSearchKeyword()),
                        categoryEq(searchKeywordDTO.getCategory()),
                        paperStatusEq(),  // "TO_DO" 또는 "IN_PROGRESS"인 경우 필터링
                        gradeEq(searchKeywordDTO.getGrade())
                )
                .orderBy(paper.id.desc());

        long total = countTotalResults(searchKeywordDTO);

        List<Paper> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Paper> searchOCRDone(SearchKeywordDTO searchKeywordDTO, Pageable pageable) {
        JPAQuery<Paper> query = queryFactory
                .selectFrom(paper)
                .where(
                        yearEq(searchKeywordDTO.getYear()),
                        monthEq(searchKeywordDTO.getMonth()),
                        areaAndSubjectEq(searchKeywordDTO.getArea(), searchKeywordDTO.getSubject()),
                        keyWordEq(searchKeywordDTO.getSearchOption(), searchKeywordDTO.getSearchKeyword()),
                        categoryEq(searchKeywordDTO.getCategory()),
                        paperStatusDoneEq(),  // "DONE"인 경우 필터링
                        gradeEq(searchKeywordDTO.getGrade())
                )
                .orderBy(paper.id.desc());

        long total = countOCRDoneResults(searchKeywordDTO);

        List<Paper> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression yearEq(String year) {

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

    private BooleanExpression monthEq(int month) {
        BooleanExpression monthCondition = (month != 0) ? paper.month.eq(month) : null;

        // 생략해도 돌아가는지 확인하기
        if (monthCondition != null) {
            // 이전에 설정된 조건이 있을 경우에만 OR 연산을 수행
            monthCondition = monthCondition.or(paper.month.eq(month));
        }
        return monthCondition;
    }

    private BooleanExpression areaAndSubjectEq(String area, String subject) {
        if ("전체".equals(area) && "전체".equals(subject)) {
            return null;
        } else if ("전체".equals(area)) {
            return paper.area.eq(area);
        } else if (area != null) {
            BooleanExpression areaCondition = paper.area.eq(area);
            if (!"전체".equals(subject)) {
                BooleanExpression subjectCondition = paper.subject.eq(subject);
                return areaCondition.and(subjectCondition);
            }
            return areaCondition;
        }
        return null;
    }

    private BooleanExpression keyWordEq(String searchOption, String searchKeyword) {

        if ("전체".equals(searchOption) || (searchKeyword != null && searchKeyword.isEmpty())) {
            BooleanExpression userSearch = paper.user.name.like("%" + searchKeyword + "%");
            BooleanExpression nameSearch = paper.name.like("%" + searchKeyword + "%");
            return userSearch.or(nameSearch);
        } else if ("user_id".equals(searchOption) && searchKeyword != null && !searchKeyword.isEmpty()){
            BooleanExpression userSearch = Expressions.booleanTemplate("lower({0}) like lower({1})", paper.user.name, "%" + searchKeyword + "%");
            return userSearch;
        } else if ("name".equals(searchOption) && searchKeyword != null && !searchKeyword.isEmpty()){
            BooleanExpression nameSearch = Expressions.booleanTemplate("lower({0}) like lower({1})", paper.name, "%" + searchKeyword + "%");
            return nameSearch;
        } else {
            // 두 값 모두 넘어오지 않았을 경우
            return null;
        }
    }

    private BooleanExpression categoryEq(String category) {
        System.out.println("레파지토리 부분 - 받은 category 값 : " + category);
        BooleanExpression categoryCondition = (category != null) ? paper.category.eq(category) : null;
        return categoryCondition;
    }

    private BooleanExpression gradeEq(String grade) {

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

    private BooleanExpression paperStatusEq() {
        return paper.paperStatus.in(PaperStatus.TO_DO, PaperStatus.IN_PROGRESS);
    }

    private BooleanExpression paperStatusDoneEq() {
        return paper.paperStatus.in(PaperStatus.DONE);
    }

    private Long countTotalResults(SearchKeywordDTO searchKeywordDTO) {
        JPAQuery<Paper> query = queryFactory
                .selectFrom(paper)
                .where(
                        yearEq(searchKeywordDTO.getYear()),
                        monthEq(searchKeywordDTO.getMonth()),
                        areaAndSubjectEq(searchKeywordDTO.getArea(), searchKeywordDTO.getSubject()),
                        keyWordEq(searchKeywordDTO.getSearchOption(), searchKeywordDTO.getSearchKeyword()),
                        categoryEq(searchKeywordDTO.getCategory()),
                        paperStatusEq(),
                        gradeEq(searchKeywordDTO.getGrade())
                );
        return query.fetchCount();
    }

    private Long countOCRDoneResults(SearchKeywordDTO searchKeywordDTO) {
        JPAQuery<Paper> query = queryFactory
                .selectFrom(paper)
                .where(
                        yearEq(searchKeywordDTO.getYear()),
                        monthEq(searchKeywordDTO.getMonth()),
                        areaAndSubjectEq(searchKeywordDTO.getArea(), searchKeywordDTO.getSubject()),
                        keyWordEq(searchKeywordDTO.getSearchOption(), searchKeywordDTO.getSearchKeyword()),
                        categoryEq(searchKeywordDTO.getCategory()),
                        paperStatusDoneEq(),
                        gradeEq(searchKeywordDTO.getGrade())
                );
        return query.fetchCount();
    }
}
