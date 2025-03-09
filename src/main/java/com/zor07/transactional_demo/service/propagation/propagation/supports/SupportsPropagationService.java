package com.zor07.transactional_demo.service.propagation.propagation.supports;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupportsPropagationService {

    private final TransactionRepository transactionRepository;

    public SupportsPropagationService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<TransactionLog> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }
}
