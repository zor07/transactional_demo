package com.zor07.transactional_demo.service.propagation.not_supported;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class NotSupportedTransactionLogService {

    private final TransactionLogRepository transactionLogRepository;

    public NotSupportedTransactionLogService(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void logTransaction(User user, BigDecimal amount) {
        transactionLogRepository.save(new TransactionLog(user, amount));
        throw new RuntimeException("Simulated failure");
    }
}