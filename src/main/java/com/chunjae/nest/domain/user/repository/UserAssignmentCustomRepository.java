package com.chunjae.nest.domain.user.repository;

import com.chunjae.nest.domain.user.dto.ManagerSearchDTO;
import com.chunjae.nest.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAssignmentCustomRepository {
    Page<User> getQuestionManagerByOption(ManagerSearchDTO managerSearchDTO, Pageable pageable);

}
