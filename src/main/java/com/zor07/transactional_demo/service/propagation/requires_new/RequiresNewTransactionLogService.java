package com.zor07.transactional_demo.service.propagation.requires_new;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RequiresNewTransactionLogService {

    private final TransactionLogRepository transactionLogRepository;

    public RequiresNewTransactionLogService(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logTransaction(User user, BigDecimal amount) {
        transactionLogRepository.save(new TransactionLog(user, amount));
    }
}
