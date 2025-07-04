package com.grepp.teamnotfound.app.model.user.repository;

import com.grepp.teamnotfound.app.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
