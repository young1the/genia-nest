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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void modPassword(User user, String newPassword1) {

        if (user != null) {
            System.out.println("비밀번호 : " + newPassword1);
            String encoded = EncodePasswordUtils.passwordEncoder().encode(newPassword1);
            user.setPassword(encoded);
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    public Page<User> getAllUsersOrderedByIdDesc(Pageable pageable) {
        return userRepository.findAll(pageable);
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

    public Page<User> searchUsers(String searchKeyword, String searchOption,Pageable pageable) {

        if (searchOption.equals("userId")||searchOption.equals("name")) {
            return userRepository.findByUserIdOrNameContaining(searchKeyword, pageable);
        }

        return null;
    }

    // 페이징
    public Page<User> userList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


}


