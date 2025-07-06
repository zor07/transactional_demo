package com.zor07.transactional_demo.controller.propagation;

import com.zor07.transactional_demo.service.propagation.requires_new.RequiresNewPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Propagation.REQUIRES_NEW",
        description = """
            Propagation.REQUIRES_NEW означает, что метод всегда выполняется в новой, отдельной транзакции.\s
            Если была активна внешняя транзакция, она приостанавливается на время выполнения нового метода.\s
            Новая транзакция изолирована и не зависит от внешней.
            
            Этот режим полезен, когда нужно выполнить независимую операцию, которая должна быть зафиксирована отдельно.
        """
)
@RestController
@RequestMapping("/transactions/requires-new")
public class RequiresNewTransactionController {
    private final RequiresNewPropagationService requiresNewService;

    public RequiresNewTransactionController(RequiresNewPropagationService requiresNewService) {
        this.requiresNewService = requiresNewService;
    }

    /**
     * Демонстрация поведения Propagation.REQUIRES_NEW.
     *
     * @param userId ID пользователя, для которого проводится операция
     * @param amount Сумма операции
     */
    @Operation(
            summary = "Демонстрация Propagation.REQUIRES_NEW: новая независимая транзакция",
            description = """
                    Этот метод демонстрирует поведение Propagation.REQUIRES_NEW.\s
                    Вне зависимости от внешней транзакции создаётся новая транзакция.\s
                    Внешняя транзакция приостанавливается на время выполнения.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "500", description = "Ошибка при выполнении транзакционной операции")
    })
    @PostMapping("/{userId}")
    public void testRequiresNew(
            @Parameter(description = "ID пользователя, для которого проводится операция")
            @PathVariable Long userId,

            @Parameter(description = "Сумма операции")
            @RequestParam BigDecimal amount
    ) {
        requiresNewService.processTransaction(userId, amount);
    }
}

