package com.zor07.transactional_demo.service.propagation.nested;

import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class NestedPropagationService {

    private final UserRepository userRepository;
    private final NestedTransactionLogService transactionService;

    public NestedPropagationService(UserRepository userRepository, NestedTransactionLogService transactionService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    /**
     * Внешний метод выполняется в основной транзакции.
     * Логирование транзакции выполняется во вложенной.
     * Ошибка в logTransaction НЕ откатит изменения баланса.
     */
    @Transactional
    public void processTransaction(Long userId, BigDecimal amount, boolean nestedShouldFail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        transactionService.logTransaction(user, amount, nestedShouldFail);
    }
}

