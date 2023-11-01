package com.chunjae.nest.domain.question.repository;

import com.chunjae.nest.domain.question.entity.QuestionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionLogRepository extends JpaRepository<QuestionLog, Long> {
}
