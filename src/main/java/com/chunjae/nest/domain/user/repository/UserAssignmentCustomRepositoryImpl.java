package com.chunjae.nest.domain.user.repository;

import com.chunjae.nest.domain.paper.entity.QPaper;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import com.chunjae.nest.domain.user.dto.ManagerSearchDTO;
import com.chunjae.nest.domain.user.entity.QUser;
import com.chunjae.nest.domain.user.entity.RoleStatus;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.entity.UserStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class UserAssignmentCustomRepositoryImpl implements UserAssignmentCustomRepository {
    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;

    private BooleanExpression nameLike(String name) {
        if (name == null || name.isEmpty()) return null;
        return user.name.like("%" + name + "%");
    }

    private BooleanExpression userIdLike(String id) {
        if (id == null || id.isEmpty()) return null;
        return user.userId.like("%" + id + "%");
    }

    private BooleanExpression searchByOption(ManagerSearchDTO searchKeywordDTO) {
        System.out.println(searchKeywordDTO);
        if (searchKeywordDTO == null) return null;
        String keyword = searchKeywordDTO.getSearchKeyword();
        String option = searchKeywordDTO.getSearchOption();
        if (keyword == null || option == null) return null;
        if (option.equals("name")) {
            return nameLike(keyword);
        }
        if (option.equals("userId")) {
            return userIdLike(keyword);
        }
        return null;
    }

    private BooleanExpression filterDeletedManager() {
        return user.userStatus.ne(UserStatus.DELETE);
    }

    private BooleanExpression filterCancelManger() {
        return user.role.roleStatus.ne(RoleStatus.CANCELLED);
    }

    private BooleanExpression filterTerminateManager() {
        return user.role.roleStatus.ne(RoleStatus.TERMINATED);
    }

    private BooleanExpression filterQuestionManager() {
        return user.role.role.eq("문제담당자");
    }

    @Override
    public Page<User> getQuestionManagerByOption(ManagerSearchDTO managerSearchDTO, Pageable pageable) {
        JPAQuery<User> query = queryFactory.from(user).select(user).where(filterQuestionManager(), searchByOption(managerSearchDTO), filterDeletedManager(), filterCancelManger(), filterTerminateManager());
        Long count = queryFactory.from(user).select(user.count()).where(filterQuestionManager(), searchByOption(managerSearchDTO), filterDeletedManager(), filterCancelManger(), filterTerminateManager()).fetchOne();
        List<User> result = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        return new PageImpl<>(result, pageable, count);
    }
}
