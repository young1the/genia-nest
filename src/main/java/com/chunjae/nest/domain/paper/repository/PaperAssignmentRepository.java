package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.entity.PaperAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperAssignmentRepository extends JpaRepository<PaperAssignment, Long> {
}
