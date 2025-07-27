package com.zor07.transactional_demo.controller.proxy;

import com.zor07.transactional_demo.service.proxy.ProxyDemoTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Транзакции (прокси)",
        description = """
            Этот контроллер демонстрирует работу транзакций через ручной прокси.\s
            Вместо использования аннотации @Transactional, транзакционное поведение реализовано вручную с помощью JDK Proxy и TransactionManager.\s
            Это позволяет увидеть внутренний механизм работы транзакций в Spring "под капотом".
            \n\n
            Все методы, вызываемые через прокси, оборачиваются в транзакцию вручную: перед вызовом метода открывается транзакция,\s
            при успешном завершении — коммит, при исключении — откат.
        """
)
@RestController
@RequestMapping("/proxy-demo-transactions")
public class ProxyDemoTransactionController {

    private final ProxyDemoTransactionService proxyDemoTransactionService;



    public ProxyDemoTransactionController(ProxyDemoTransactionService proxyDemoTransactionService) {
        this.proxyDemoTransactionService = proxyDemoTransactionService;
    }

    @Operation(
            summary = "Выполнить транзакцию через прокси",
            description = """
                Демонстрация ручного управления транзакцией через JDK Proxy.\s
                Метод вызывает обновление баланса пользователя и добавление записи в лог.\s
                Если передать параметр `throwError=true`, будет выброшено исключение, чтобы показать, как происходит откат транзакции вручную.
                \n\n
                Используется {@code PlatformTransactionManager} внутри {@code InvocationHandler}, который вручную открывает, коммитит или откатывает транзакцию.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транзакция успешно выполнена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, транзакция откатилась")
    })
    @PostMapping("/{userId}")
    public void processTransaction(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "Сумма транзакции") @RequestParam BigDecimal amount,
            @Parameter(description = "Генерировать ошибку для демонстрации отката")
            @RequestParam(required = false, defaultValue = "false") boolean shouldThrowException) {
        proxyDemoTransactionService.processTransaction(userId, amount, shouldThrowException);
    }
}
