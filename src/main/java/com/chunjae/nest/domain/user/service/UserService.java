package com.chunjae.nest.domain.user.service;

import com.chunjae.nest.domain.user.dto.CreateUserReqDTO;
import com.chunjae.nest.domain.user.entity.Role;
import com.chunjae.nest.domain.user.entity.RoleStatus;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.entity.UserStatus;
import com.chunjae.nest.domain.user.repository.RoleRepository;
import com.chunjae.nest.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User createUser(CreateUserReqDTO userDTO){

User user = User.builder()
        .userId(userDTO.getUserId())
        .password(userDTO.getUserId())
        .name(userDTO.getName())
        .userStatus(UserStatus.NEW_USER)
        .part("디지털사업부").build();
        userRepository.save(user);

        LocalDateTime now = LocalDateTime.now();

        Role role = Role.builder()
                .startDate(now)
                .endDate(now.plusYears(1))
                .role("총괄관리자")
                .roleStatus(RoleStatus.PENDING)
                .build();
        roleRepository.save(role);

        return user;
    }

}
