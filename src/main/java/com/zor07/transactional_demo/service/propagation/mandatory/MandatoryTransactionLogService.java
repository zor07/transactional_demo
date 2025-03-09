package com.zor07.transactional_demo.service.propagation.mandatory;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionRepository;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MandatoryTransactionLogService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public MandatoryTransactionLogService(TransactionRepository transactionRepository,
                                          UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void logTransaction(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        transactionRepository.save(new TransactionLog(user, amount));
    }
}
