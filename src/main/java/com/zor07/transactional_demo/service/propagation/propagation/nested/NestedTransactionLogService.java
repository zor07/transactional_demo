package com.zor07.transactional_demo.service.propagation.propagation.nested;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class NestedTransactionLogService {

    private final TransactionRepository transactionRepository;

    public NestedTransactionLogService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(propagation = Propagation.NESTED)
    public void logTransaction(User user, BigDecimal amount) {
        transactionRepository.save(new TransactionLog(user, amount));
        throw new RuntimeException("Simulated nested transaction failure");
    }
}