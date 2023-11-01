package com.chunjae.nest.domain.user.repository;

import com.chunjae.nest.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);

   /* userId 또는 name에 searchKeyword가 포함되고,
    사용자 상태가 'DELETE'가 아니며,
    역할 상태가 'CANCELLED' 또는 'TERMINATED'가 아닌 사용자*/
   @Query("SELECT u FROM User u " +
           "WHERE (u.userId LIKE %:searchKeyword% OR u.name LIKE %:searchKeyword%) " +
           "AND u.userStatus != 'DELETE' " +
           "AND (u.role.roleStatus != 'CANCELLED' OR u.role.roleStatus != 'TERMINATED')")
   Page<User> findByUserIdOrNameContaining(@Param("searchKeyword") String searchKeyword, Pageable pageable);

    @Query("SELECT u FROM User u " +
            "WHERE 1=1" +
            "AND u.userStatus != 'DELETE' " +
            "AND (u.role.roleStatus != 'CANCELLED' OR u.role.roleStatus != 'TERMINATED')")
   Page<User> findAll(Pageable pageable);


}
