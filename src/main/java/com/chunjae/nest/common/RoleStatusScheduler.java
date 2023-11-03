package com.chunjae.nest.common;

import com.chunjae.nest.domain.user.entity.Role;
import com.chunjae.nest.domain.user.entity.RoleStatus;
import com.chunjae.nest.domain.user.repository.RoleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RoleStatusScheduler {

    private final RoleRepository roleRepository;

    public RoleStatusScheduler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void updateRoleStatus() {
        Date currentDate = new Date();

        List<Role> roles = roleRepository.findByEndDateBeforeAndRoleStatusNot(currentDate, RoleStatus.TERMINATED);
        for (Role role : roles) {
            role.setRoleStatus(RoleStatus.TERMINATED);
            roleRepository.save(role);
        }
    }

}
