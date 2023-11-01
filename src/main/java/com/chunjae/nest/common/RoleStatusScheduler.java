package com.chunjae.nest.common;

import com.chunjae.nest.domain.user.repository.RoleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import com.chunjae.nest.domain.user.entity.Role;
import com.chunjae.nest.domain.user.entity.RoleStatus;

@Component
public class RoleStatusScheduler {

    private final RoleRepository roleRepository;

    public RoleStatusScheduler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Scheduled(cron = "*/10 * * * * *") // 테스트할려고 10초 마다실행으로 변경함

    public void updateRoleStatus() {
        Date currentDate = new Date();

        // 기간 만료된 역할을 찾아서 업데이트
        List<Role> roles = roleRepository.findByEndDateBeforeAndRoleStatusNot(currentDate, RoleStatus.TERMINATED);
        for (Role role : roles) {
            role.setRoleStatus(RoleStatus.TERMINATED);
            roleRepository.save(role);
        }
        // 또 뭐있지?
    }

}
