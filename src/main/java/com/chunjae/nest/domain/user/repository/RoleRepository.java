package com.chunjae.nest.domain.user.repository;

import com.chunjae.nest.domain.user.entity.Role;
import com.chunjae.nest.domain.user.entity.RoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByEndDateBeforeAndRoleStatusNot(Date endDate, RoleStatus roleStatus);
}