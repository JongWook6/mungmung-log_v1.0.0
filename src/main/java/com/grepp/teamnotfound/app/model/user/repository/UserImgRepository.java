package com.grepp.teamnotfound.app.model.user.repository;

import com.grepp.teamnotfound.app.model.user.entity.UserImg;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserImgRepository extends JpaRepository<UserImg, Long> {

    @Query("SELECT ui FROM UserImg ui " +
            "JOIN FETCH ui.user " +
            "WHERE ui.user.userId = :userId")
    UserImg findByUserImgWithUser(@Param("userId") Long userId);

    Optional<UserImg> findByUser_UserIdAndDeletedAtIsNull(Long userId);
}
