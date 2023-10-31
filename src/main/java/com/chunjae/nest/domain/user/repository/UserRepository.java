package com.chunjae.nest.domain.user.repository;

import com.chunjae.nest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);

    @Query("SELECT u FROM User u WHERE u.userId LIKE %:searchKeyword%")
    List<User> findByUserIdContaining(@Param("searchKeyword") String searchKeyword);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:searchKeyword%")
    List<User> findByNameContaining(@Param("searchKeyword") String searchKeyword);
}
