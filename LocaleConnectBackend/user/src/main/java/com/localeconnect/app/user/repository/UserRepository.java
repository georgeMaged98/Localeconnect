package com.localeconnect.app.user.repository;

import com.localeconnect.app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
