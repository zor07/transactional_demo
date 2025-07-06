package com.zor07.transactional_demo.controller.proxy;

import com.zor07.transactional_demo.service.proxy.ProxyDemoTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Транзакции (прокси)", description = "Демонстрация работы транзакций через ручной прокси")
@RestController
@RequestMapping("/proxy-demo-transactions")
public class ProxyDemoTransactionController {

    private final ProxyDemoTransactionService proxyDemoTransactionService;



    public ProxyDemoTransactionController(ProxyDemoTransactionService proxyDemoTransactionService) {
        this.proxyDemoTransactionService = proxyDemoTransactionService;
    }

    @Operation(summary = "Выполнить транзакцию через прокси", description = "Обновляет баланс пользователя и добавляет лог транзакции. Может сгенерировать исключение.")
    @PostMapping("/{userId}")
    public void processTransaction(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "Сумма транзакции") @RequestParam BigDecimal amount,
            @Parameter(description = "Генерировать ошибку для демонстрации отката")
            @RequestParam(required = false, defaultValue = "false") boolean throwError) {
        proxyDemoTransactionService.processTransaction(userId, amount, throwError);
    }
}
