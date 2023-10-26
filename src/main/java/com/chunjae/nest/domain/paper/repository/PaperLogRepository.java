package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.entity.PaperLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperLogRepository extends JpaRepository<PaperLog, Long> {
}
