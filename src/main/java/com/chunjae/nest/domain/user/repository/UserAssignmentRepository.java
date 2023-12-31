package com.chunjae.nest.domain.user.repository;

import com.chunjae.nest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAssignmentRepository extends JpaRepository<User, Long>, UserAssignmentCustomRepository {
}
