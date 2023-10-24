package com.chunjae.nest.domain.paper.repository;
import com.chunjae.nest.domain.paper.entity.Paper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, Long>, PaperRepositoryCustom{

}
