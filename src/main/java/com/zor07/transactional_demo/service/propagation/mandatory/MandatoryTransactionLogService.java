package com.zor07.transactional_demo.service.propagation.mandatory;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionLogRepository;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MandatoryTransactionLogService {

    private final TransactionLogRepository transactionLogRepository;
    private final UserRepository userRepository;

    public MandatoryTransactionLogService(TransactionLogRepository transactionLogRepository,
                                          UserRepository userRepository) {
        this.transactionLogRepository = transactionLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void logTransaction(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        transactionLogRepository.save(new TransactionLog(user, amount));
    }
}
