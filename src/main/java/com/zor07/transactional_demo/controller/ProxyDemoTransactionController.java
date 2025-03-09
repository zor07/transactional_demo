package com.zor07.transactional_demo.controller;

import com.zor07.transactional_demo.service.proxy.ProxyDemoTransactionService;
import com.zor07.transactional_demo.service.proxy.TransactionalProxyHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Транзакции (прокси)", description = "Демонстрация работы транзакций через ручной прокси")
@RestController
@RequestMapping("/proxy-demo-transactions")
public class ProxyDemoTransactionController {

    private final ProxyDemoTransactionService transactionServiceProxy;

    public ProxyDemoTransactionController(ProxyDemoTransactionService transactionService,
                                          PlatformTransactionManager transactionManager) {
        this.transactionServiceProxy = TransactionalProxyHandler.createProxy(
                transactionService,
                ProxyDemoTransactionService.class,
                transactionManager
        );
    }

    @Operation(summary = "Выполнить транзакцию через прокси", description = "Обновляет баланс пользователя и добавляет лог транзакции. Может сгенерировать исключение.")
    @PostMapping("/{userId}")
    public void processTransaction(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "Сумма транзакции") @RequestParam BigDecimal amount,
            @Parameter(description = "Генерировать ошибку для демонстрации отката")
            @RequestParam(required = false, defaultValue = "false") boolean throwError) {
        transactionServiceProxy.processTransaction(userId, amount, throwError);
    }
}
