package com.zor07.transactional_demo.controller;

import com.zor07.transactional_demo.service.propagation.not_supported.NotSupportedPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Propagation.NOT_SUPPORTED",
        description = """
            Propagation.NOT_SUPPORTED означает, что метод не должен выполняться в рамках транзакции.\s
            Если вызывается внутри транзакции, эта транзакция приостанавливается на время выполнения метода.\s
            Метод выполняется вне транзакционной обёртки.
            
            Такой режим полезен, когда операция должна быть выполнена без транзакции, например, для запросов к внешним системам или логирования.
        """
)
@RestController
@RequestMapping("/transactions/not-supported")
public class NotSupportedTransactionController {
    private final NotSupportedPropagationService notSupportedService;

    public NotSupportedTransactionController(NotSupportedPropagationService notSupportedService) {
        this.notSupportedService = notSupportedService;
    }

    /**
     * Демонстрация поведения Propagation.NOT_SUPPORTED.
     *
     * @param userId ID пользователя, для которого проводится операция
     * @param amount Сумма операции
     */
    @Operation(
            summary = "Демонстрация Propagation.NOT_SUPPORTED: выполнение вне транзакции",
            description = """
                    Этот метод демонстрирует поведение Propagation.NOT_SUPPORTED.\s
                    Если вызывается внутри транзакции — она будет приостановлена на время выполнения метода.\s
                    Метод выполнится без транзакции.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "500", description = "Ошибка при выполнении операции вне транзакции")
    })
    @PostMapping("/{userId}")
    public void testNotSupported(
            @Parameter(description = "ID пользователя, для которого проводится операция")
            @PathVariable Long userId,

            @Parameter(description = "Сумма операции")
            @RequestParam BigDecimal amount
    ) {
        notSupportedService.processTransaction(userId, amount);
    }
}
