package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.entity.PaperFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperFileRepository extends JpaRepository<PaperFile, Long> {
}
