package com.zor07.transactional_demo.service.propagation.not_supported;

import com.zor07.transactional_demo.entity.User;
import com.zor07.transactional_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class NotSupportedPropagationService {

    private final UserRepository userRepository;
    private final NotSupportedTransactionLogService transactionService;

    public NotSupportedPropagationService(UserRepository userRepository, NotSupportedTransactionLogService transactionService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public void processTransaction(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        transactionService.logTransaction(user, amount);
    }
}
