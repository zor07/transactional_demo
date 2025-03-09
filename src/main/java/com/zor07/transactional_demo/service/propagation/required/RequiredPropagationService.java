package com.zor07.transactional_demo.service.propagation.required;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.repository.TransactionRepository;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RequiredPropagationService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public RequiredPropagationService(UserRepository userRepository,
                                      TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * В этом примере метод выполняется в рамках одной транзакции.
     * Если логика внутри метода приведет к исключению, откатится весь процесс.
     */
    @Transactional
    public void processTransaction(Long userId, BigDecimal amount) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        transactionRepository.save(new TransactionLog(user, amount));

        // Искусственно выбрасываем ошибку для демонстрации отката
        if (amount.compareTo(new BigDecimal(1000)) > 0) throw new RuntimeException("Transaction rollback simulation");
    }

}
