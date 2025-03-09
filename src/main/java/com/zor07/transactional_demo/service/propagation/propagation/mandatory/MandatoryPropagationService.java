package com.zor07.transactional_demo.service.propagation.propagation.mandatory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MandatoryPropagationService {

    private final MandatoryTransactionLogService transactionService;

    public MandatoryPropagationService(MandatoryTransactionLogService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * В этом методе есть транзакция, поэтому logTransaction выполнится корректно.
     */
    @Transactional
    public void processWithTransaction(Long userId, BigDecimal amount) {
        transactionService.logTransaction(userId, amount);
    }

    /**
     * Без @Transactional этот метод вызовет исключение при обращении к logTransaction.
     */
    public void processWithoutTransaction(Long userId, BigDecimal amount) {
        transactionService.logTransaction(userId, amount);
    }
}
