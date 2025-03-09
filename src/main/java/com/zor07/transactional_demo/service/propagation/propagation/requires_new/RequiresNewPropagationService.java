package com.zor07.transactional_demo.service.propagation.propagation.requires_new;

import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RequiresNewPropagationService {

    private final UserRepository userRepository;
    private final RequiresNewTransactionLogService transactionService;

    public RequiresNewPropagationService(UserRepository userRepository, RequiresNewTransactionLogService transactionService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    /**
     * Метод processTransaction создает одну транзакцию,
     * а метод logTransaction создает новую (отдельную) транзакцию.
     * Если processTransaction завершится с ошибкой, logTransaction НЕ откатится.
     */
    @Transactional
    public void processTransaction(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        transactionService.logTransaction(user, amount);

        throw new RuntimeException("Transaction rollback simulation");
    }
}



