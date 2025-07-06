package com.zor07.transactional_demo.repository;

import com.zor07.transactional_demo.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByUserId(Long userId);
}
