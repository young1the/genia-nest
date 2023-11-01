package com.chunjae.nest.domain.question.repository;

import com.chunjae.nest.domain.question.entity.QuestionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionFileRepository extends JpaRepository<QuestionFile, Long> {
}
