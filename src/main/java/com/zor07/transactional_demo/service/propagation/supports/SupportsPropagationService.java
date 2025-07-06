package com.zor07.transactional_demo.service.propagation.supports;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupportsPropagationService {

    private final TransactionLogRepository transactionLogRepository;

    public SupportsPropagationService(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<TransactionLog> getUserTransactions(Long userId) {
        return transactionLogRepository.findByUserId(userId);
    }
}
