package com.zor07.transactional_demo.service.propagation.required;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionLogRepository;
import com.zor07.transactional_demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RequiredPropagationService {

    private static final Logger log = LoggerFactory.getLogger(RequiredPropagationService.class);

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
        // 1. Обновляем баланс пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден!"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        log.info("✅ Баланс пользователя обновлён: {}", user.getBalance());

        // 2. Эмулируем ошибку
        if (shouldThrowException) {
            throw new RuntimeException("❌ Ошибка! Должен произойти откат.");
        }

        // 3. Записываем лог транзакции
        TransactionLog transactionLog = new TransactionLog(user, amount);
        transactionLogRepository.save(transactionLog);
        log.info("📝 Лог транзакции записан.");
    }

}
