package com.zor07.transactional_demo.service.proxy;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.TransactionRepository;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProxyDemoTransactionServiceImpl implements ProxyDemoTransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionLogRepository;

    public ProxyDemoTransactionServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionLogRepository = transactionRepository;
    }

    @Override
    public void processTransaction(Long userId, BigDecimal amount, boolean throwError) {
        // 1. Обновляем баланс пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден!"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        System.out.println("✅ Баланс пользователя обновлён: " + user.getBalance());

        // 2. Записываем лог транзакции
        TransactionLog log = new TransactionLog(user, amount);
        transactionLogRepository.save(log);
        System.out.println("📝 Лог транзакции записан.");

        // 3. Эмулируем ошибку
        if (throwError) {
            throw new RuntimeException("❌ Ошибка! Должен произойти откат.");
        }
    }
}
