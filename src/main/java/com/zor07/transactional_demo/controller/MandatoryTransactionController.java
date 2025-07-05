package com.zor07.transactional_demo.controller;

import com.zor07.transactional_demo.service.propagation.mandatory.MandatoryPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Propagation.MANDATORY",
        description = """
            Если метод, помеченный @Transactional(propagation = MANDATORY), вызывается вне активной транзакции,\s
            Spring выбрасывает исключение IllegalTransactionStateException.

            Это поведение полезно, когда вы хотите быть уверены, что вызов происходит строго внутри уже существующей транзакции,\s
            например — как часть более широкой бизнес-операции.
        """
)
@RestController
@RequestMapping("/transactions/mandatory")
public class MandatoryTransactionController {
    private final MandatoryPropagationService mandatoryService;

    public MandatoryTransactionController(MandatoryPropagationService mandatoryService) {
        this.mandatoryService = mandatoryService;
    }

    /**
     * Демонстрация поведения Propagation.MANDATORY.
     *
     * @param userId         ID пользователя, для которого проводится операция
     * @param amount         Сумма операции
     * @param withTransaction Если true — метод вызывается внутри @Transactional-обёртки (ошибки не будет).
     *                        Если false — метод вызывается напрямую, без транзакции, и приведёт к исключению.
     */
    @Operation(
            summary = "Демонстрация Propagation.MANDATORY: требует активную транзакцию",
            description = """
                    Этот метод демонстрирует поведение Propagation.MANDATORY.
            
                    Параметр `withTransaction = true` — оборачивает вызов в новую транзакцию.
                    Параметр `withTransaction = false` — вызывает метод без транзакции и приводит к исключению
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "500", description = "Ошибка: транзакция обязательна, но не найдена (MANDATORY)")
    })
    @PostMapping("/{userId}")
    public void testMandatory(
            @Parameter(description = "ID пользователя, для которого проводится операция")
            @PathVariable Long userId,

            @Parameter(description = "Сумма операции")
            @RequestParam BigDecimal amount,

            @Parameter(description = """
                true — метод вызывается внутри обёртки с @Transactional.
                false — метод вызывается напрямую, что приводит к ошибке при использовании MANDATORY.
                """)
            @RequestParam(required = false, defaultValue = "false") boolean withTransaction
    ) {
        if (withTransaction) {
            mandatoryService.processWithTransaction(userId, amount);
        } else {
            mandatoryService.processWithoutTransaction(userId, amount);
        }
    }
}
