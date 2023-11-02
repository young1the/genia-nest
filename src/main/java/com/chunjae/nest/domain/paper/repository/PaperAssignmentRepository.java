package com.chunjae.nest.domain.paper.repository;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperAssignment;
import com.chunjae.nest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaperAssignmentRepository extends JpaRepository<PaperAssignment, Long> {
    Optional<PaperAssignment> findByUserAndPaper(User user, Paper paper);
}
