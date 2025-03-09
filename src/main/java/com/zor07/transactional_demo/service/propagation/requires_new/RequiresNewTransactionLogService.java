package com.zor07.transactional_demo.service.propagation.requires_new;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RequiresNewTransactionLogService {

    private final TransactionRepository transactionRepository;

    public RequiresNewTransactionLogService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logTransaction(User user, BigDecimal amount) {
        transactionRepository.save(new TransactionLog(user, amount));
    }
}
