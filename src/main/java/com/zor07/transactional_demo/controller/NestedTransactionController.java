package com.zor07.transactional_demo.controller;

import com.zor07.transactional_demo.service.propagation.nested.NestedPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Propagation.NESTED",
        description = """
            Propagation.NESTED создаёт вложенную транзакцию внутри существующей транзакции.\s
            Если внешняя транзакция откатывается, откатывается и вложенная.\s
            При откате вложенной транзакции без отката внешней — изменения вложенной транзакции откатываются, но внешняя продолжается.
            
            Реализация вложенных транзакций требует поддержки Savepoint в базе данных.
        """
)
@RestController
@RequestMapping("/transactions/nested")
public class NestedTransactionController {
    private final NestedPropagationService nestedService;

    public NestedTransactionController(NestedPropagationService nestedService) {
        this.nestedService = nestedService;
    }

    /**
     * Демонстрация поведения Propagation.NESTED.
     *
     * @param userId ID пользователя, для которого проводится операция
     * @param amount Сумма операции
     */
    @Operation(
            summary = "Демонстрация Propagation.NESTED: вложенная транзакция",
            description = """
                    Этот метод демонстрирует поведение Propagation.NESTED.\s
                    Вызов происходит внутри существующей транзакции, создаётся вложенная транзакция (Savepoint).\s
                    При ошибке во вложенной транзакции она откатывается, но внешняя может продолжиться.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "500", description = "Ошибка во вложенной транзакции, откат Savepoint")
    })
    @PostMapping("/{userId}")
    public void testNested(
            @Parameter(description = "ID пользователя, для которого проводится операция")
            @PathVariable Long userId,

            @Parameter(description = "Сумма операции")
            @RequestParam BigDecimal amount,

            @Parameter(description = """
                true — в методе вложенной будет брошен exception.
                false — в методе вложенной будет не брошен exception.
                """)
            @RequestParam(required = false, defaultValue = "false") boolean nestedShouldFail
    ) {
        nestedService.processTransaction(userId, amount, nestedShouldFail);
    }
}

