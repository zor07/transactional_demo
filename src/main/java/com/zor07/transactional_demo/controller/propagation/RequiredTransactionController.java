package com.zor07.transactional_demo.controller.propagation;

import com.zor07.transactional_demo.service.propagation.required.RequiredPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Propagation.REQUIRED",
        description = """
            Propagation.REQUIRED — это стандартное поведение Spring по умолчанию.\s
            Метод выполняется в существующей транзакции, если она есть,\s
            либо создаёт новую транзакцию, если вызов происходит вне транзакции.
            
            Этот режим обеспечивает атомарность и согласованность операций.
        """
)
@RestController
@RequestMapping("/transactions/required")
public class RequiredTransactionController {
    private final RequiredPropagationService requiredService;

    public RequiredTransactionController(RequiredPropagationService requiredService) {
        this.requiredService = requiredService;
    }

    /**
     * Демонстрация поведения Propagation.REQUIRED.
     *
     * @param userId ID пользователя, для которого проводится операция
     * @param amount Сумма операции
     */
    @Operation(
            summary = "Демонстрация Propagation.REQUIRED: транзакция создаётся или используется существующая",
            description = """
                    Этот метод демонстрирует поведение Propagation.REQUIRED.\s
                    Если вызывается внутри транзакции — будет использоваться существующая транзакция.\s
                    Если вызывается вне транзакции — создаётся новая.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "500", description = "Ошибка при выполнении транзакционной операции")
    })
    @PostMapping("/{userId}")
    public void testRequired(
            @Parameter(description = "ID пользователя, для которого проводится операция")
            @PathVariable Long userId,

            @Parameter(description = "Сумма операции")
            @RequestParam BigDecimal amount
    ) {
        requiredService.processTransaction(userId, amount);
    }
}

