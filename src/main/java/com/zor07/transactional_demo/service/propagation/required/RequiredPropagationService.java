package com.zor07.transactional_demo.service.propagation.required;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.repository.TransactionLogRepository;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RequiredPropagationService {
    private final UserRepository userRepository;
    private final TransactionLogRepository transactionLogRepository;

    public RequiredPropagationService(UserRepository userRepository,
                                      TransactionLogRepository transactionLogRepository) {
        this.userRepository = userRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    /**
     * В этом примере метод выполняется в рамках одной транзакции.
     * Если логика внутри метода приведет к исключению, откатится весь процесс.
     */
    @Transactional
    public void processTransaction(Long userId, BigDecimal amount, boolean shouldThrowException) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        // Искусственно выбрасываем ошибку для демонстрации отката
        if (shouldThrowException) throw new RuntimeException("Transaction rollback simulation");

        transactionLogRepository.save(new TransactionLog(user, amount));
    }

}
