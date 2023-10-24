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
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User createUser(CreateUserReqDTO userDTO) {

        User user = User.builder()
                .userId(userDTO.getUserId())
                .password(userDTO.getUserId())
                .name(userDTO.getName())
                .userStatus(UserStatus.NEW_USER)
                .part(userDTO.getPart()).build();
        User dbUser = userRepository.save(user);



        Role role = Role.builder()
                .user(dbUser)
                .role(userDTO.getRole())
                .startDate(userDTO.getStartDate())
                .endDate(userDTO.getEndDate())
                .roleStatus(RoleStatus.PENDING)
                .build();
        roleRepository.save(role);

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
