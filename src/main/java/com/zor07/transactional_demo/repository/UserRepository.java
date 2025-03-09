package com.zor07.transactional_demo.repository;

import com.zor07.transactional_demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
