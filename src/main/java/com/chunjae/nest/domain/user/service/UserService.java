package com.chunjae.nest.domain.user.service;

import com.chunjae.nest.common.util.EncodePasswordUtils;
import com.chunjae.nest.domain.user.dto.CreateUserReqDTO;
import com.chunjae.nest.domain.user.entity.Role;
import com.chunjae.nest.domain.user.entity.RoleStatus;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.entity.UserStatus;
import com.chunjae.nest.domain.user.repository.RoleRepository;
import com.chunjae.nest.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User createUser(CreateUserReqDTO userDTO) {
        User user = User.builder()
                .userId(userDTO.getUserId())
                .name(userDTO.getName())
                .password(EncodePasswordUtils.passwordEncoder().encode(userDTO.getUserId()))
                .userStatus(UserStatus.NEW_USER)
                .part(userDTO.getPart())
                .build();

        // 비밀번호를 암호화하여 저장
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


    public User login(String userId, String password) {
        // 회원 정보 & 비밀번호 조회
        User user = userRepository.findByUserId(userId);
        if (user == null) return null;
        if (!EncodePasswordUtils.passwordEncoder().matches(password, user.getPassword())) {
            return null;
        }
        if (user.getRole().getRoleStatus() == RoleStatus.CANCELLED || user.getRole().getRoleStatus() == RoleStatus.TERMINATED) {
            return null;
        }
        return user;
    }

    public UserStatus getCurrentUserStatus(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return user.getUserStatus();
        }
        return null;
    }

    public void modPassword(User user, String newPassword) {

        if (user != null) {
            System.out.println("비밀번호 : " + newPassword);
            String encoded = EncodePasswordUtils.passwordEncoder().encode(newPassword);
            user.setPassword(encoded);
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    public List<User> getAllUsersOrderedByIdDesc() {
        return userRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Transactional
    public void resetUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        System.out.println(user.getUserId());
        if (user != null) {
            String encoded = EncodePasswordUtils.passwordEncoder().encode(user.getUserId());
            user.setPassword(encoded);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.setUserStatus(UserStatus.DELETE);
        Role role = user.getRole();
        role.setRoleStatus(RoleStatus.CANCELLED);//권한 삭제
        userRepository.save(user);
        log.info("user: {}", user.getUserStatus());
    }

    public List<User> searchUsers(String searchKeyword, String searchOption) {

        if (searchOption.equals("userId")) {

            return userRepository.findByUserIdContaining(searchKeyword)
                    .stream()
                    .filter(user -> user.getUserStatus() != UserStatus.DELETE && (user.getRole().getRoleStatus() != RoleStatus.CANCELLED || user.getRole().getRoleStatus() != RoleStatus.TERMINATED))
                    .toList();
        }
            // 유저 레포지토리에서 userId가 searchKeyword로 받은 값을 가지고 있는 유저 가져오기
        if (searchOption.equals("name")) {
            // 유저 레포지토리에서 name이 searchKeyword로 받은 값을 가지고 있는 유저 가져오기

            return userRepository.findByNameContaining(searchKeyword)
                    .stream()
                    .filter(user -> user.getUserStatus() != UserStatus.DELETE && (user.getRole().getRoleStatus() != RoleStatus.CANCELLED || user.getRole().getRoleStatus() != RoleStatus.TERMINATED))
                    .toList();
        }
        return null;

    }

}


