package com.chunjae.nest.domain.question.repository;

import com.chunjae.nest.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByPaperIdAndNum(Long paperId, int num);
}
