package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.paper.entity.QPaper;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaperRepositoryImpl implements PaperRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QPaper paper = QPaper.paper;

    private BooleanExpression dateAfter(String date) {
        if (date == null || date.isEmpty()) return null;
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.of(0, 0, 0);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return paper.createdAt.after(localDateTime);
    }

    private BooleanExpression dateBefore(String date) {
        if (date == null || date.isEmpty()) return null;
        LocalDate localDate = LocalDate.parse(date).plusDays(1);
        LocalTime localTime = LocalTime.of(0, 0, 0);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return paper.createdAt.before(localDateTime);
    }

    private BooleanExpression nameLike(String name) {
        if (name == null || name.isEmpty()) return null;
        return paper.user.name.like("%" + name + "%");
    }

    private BooleanExpression userIdLike(String id) {
        if (id == null || id.isEmpty()) return null;
        return paper.user.userId.like("%" + id + "%");
    }

    private BooleanExpression searchByOption(AssignmentSearchReqDTO searchKeywordDTO) {
        if (searchKeywordDTO == null) return null;
        String keyword = searchKeywordDTO.getSearchKeyword();
        String option = searchKeywordDTO.getSearchOption();
        if (keyword == null || option == null || keyword.isEmpty() || option.isEmpty() || keyword.isBlank() || option.isBlank()) return null;
        if (option.equals("name")) {
            return nameLike(keyword);
        }
        if (option.equals("userId")) {
            return userIdLike(keyword);
        }
        if (option.equals("all")) {
            return nameLike(keyword).or(userIdLike(keyword));
        }
        return null;
    }

    public Page<Paper> searchByWhere(AssignmentSearchReqDTO searchKeywordDTO, Pageable pageable) {
        JPAQuery<Paper> query = queryFactory.selectFrom(paper);
        query = query.where(
                searchByOption(searchKeywordDTO),
                dateAfter(searchKeywordDTO.getStartDate()),
                dateBefore(searchKeywordDTO.getEndDate())).
                orderBy(paper.id.desc());
        Long count = queryFactory.selectFrom(paper).select(paper.count()).where(
                searchByOption(searchKeywordDTO),
                dateAfter(searchKeywordDTO.getStartDate()),
                dateBefore(searchKeywordDTO.getEndDate())).fetchOne();
        List<Paper> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(results, pageable, count);
    }

    public Page<Paper> searchByWhere(SearchKeywordDTO searchKeywordDTO, Pageable pageable) {
        JPAQuery<Paper> query = queryFactory
                .selectFrom(paper)
                .where(
                        yearEq(searchKeywordDTO.getYear()),
                        monthEq(searchKeywordDTO.getMonth()),
                        areaAndSubjectEq(searchKeywordDTO.getArea(), searchKeywordDTO.getSubject()),
                        keyWordEq(searchKeywordDTO.getSearchOption(), searchKeywordDTO.getSearchKeyword()),
                        categoryEq(searchKeywordDTO.getCategory()),
                        paperStatusEq(searchKeywordDTO.getPaperStatus()),
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
        //System.out.println("레파지토리 부분 - 받은 month 값 : " + month);
        BooleanExpression monthCondition = (month != 0) ? paper.month.eq(month) : null;

        // 생략해도 돌아가는지 확인하기
        if (monthCondition != null) {
            // 이전에 설정된 조건이 있을 경우에만 OR 연산을 수행
            monthCondition = monthCondition.or(paper.month.eq(month));
        }
        return monthCondition;
    }

    private BooleanExpression areaAndSubjectEq(String area, String subject) {
//        System.out.println("레파지토리 부분 - 받은 area 값 : " + area);
//        System.out.println("레파지토리 부분 - 받은 subject 값 : " + subject);

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
//        System.out.println("레파지토리 부분 - 받은 keyword 값 : " + searchOption + ", " + searchKeyword);

        if ("전체".equals(searchOption) || (searchKeyword != null && searchKeyword.isEmpty())) {
            BooleanExpression userSearch = paper.user.name.like("%" + searchKeyword + "%");
            BooleanExpression nameSearch = paper.name.like("%" + searchKeyword + "%");
            return userSearch.or(nameSearch);
        } else if ("user_id".equals(searchOption) && searchKeyword != null && !searchKeyword.isEmpty()) {
            BooleanExpression userSearch = Expressions.booleanTemplate("lower({0}) like lower({1})", paper.user.name, "%" + searchKeyword + "%");
            return userSearch;
        } else if ("name".equals(searchOption) && searchKeyword != null && !searchKeyword.isEmpty()) {
            BooleanExpression nameSearch = Expressions.booleanTemplate("lower({0}) like lower({1})", paper.name, "%" + searchKeyword + "%");
            return nameSearch;
        } else {
            // 두 값 모두 넘어오지 않았을 경우
            return null;
        }
    }

    private BooleanExpression categoryEq(String category) {
//        System.out.println("레파지토리 부분 - 받은 category 값 : " + category);
        BooleanExpression categoryCondition = (category != null) ? paper.category.eq(category) : null;
        return categoryCondition;
    }

    private BooleanExpression gradeEq(String grade) {
//        System.out.println("레파지토리 부분 - 받은 grade 값 : " + grade);

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

//        System.out.println("레파지토리 부분 - 받은 paperStatus 값 : " + paperStatus);
        BooleanExpression result = paper.paperStatus.ne(PaperStatus.valueOf("DELETED"));

        if ("전체".equals(paperStatus)) {
            return result;
        } else if ("완료".equals(paperStatus)) {
            return paper.paperStatus.eq(PaperStatus.valueOf("DONE"));
        } else if ("진행중".equals(paperStatus)) {
            return paper.paperStatus.eq(PaperStatus.valueOf("IN_PROGRESS"));
        } else if ("진행 전".equals(paperStatus)) {
            return paper.paperStatus.eq(PaperStatus.valueOf("TO_DO"));
        } else {
            // 두 값 모두 넘어오지 않았을 경우
            return result;
        }
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
                        paperStatusEq(searchKeywordDTO.getPaperStatus()),
                        gradeEq(searchKeywordDTO.getGrade())
                );
        return query.fetchCount();
    }
}