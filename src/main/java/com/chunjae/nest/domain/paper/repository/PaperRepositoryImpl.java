package com.chunjae.nest.domain.paper.repository;
import com.chunjae.nest.domain.paper.dto.QSearchPaperDTO;
import com.chunjae.nest.domain.paper.dto.SearchKeywordDTO;
import com.chunjae.nest.domain.paper.dto.SearchPaperDTO;
import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.QPaper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaperRepositoryImpl implements PaperRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Paper> findAll() {
        QPaper paper = QPaper.paper;
        return queryFactory
                .selectFrom(paper)
                .fetch();
    }

    @Override
    public List<SearchPaperDTO> searchByWhere(SearchKeywordDTO searchKeywordDTO) {
        QPaper paper = QPaper.paper;
        return queryFactory
                .select(new QSearchPaperDTO(
                        paper.id,
                        paper.year,
                        paper.category,
                        paper.month,
                        paper.grade,
                        paper.area,
                        paper.subject,
                        paper.name,
                        paper.paperStatus,
                        paper.totalCount.intValue(),
                        paper.user,
                        paper.createdAt
                ))
                .from(paper)
                .where()
                .fetch();
    }
}
