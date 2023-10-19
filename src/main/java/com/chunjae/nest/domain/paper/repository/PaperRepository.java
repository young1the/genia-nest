package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.entity.Paper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findAll(Sort sort);
}
