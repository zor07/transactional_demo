package com.zor07.transactional_demo.controller.propagation;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.service.propagation.supports.SupportsPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Propagation.SUPPORTS",
        description = """
            Propagation.SUPPORTS означает, что метод будет выполняться в рамках существующей транзакции, если она есть.\s
            Если активной транзакции нет, метод выполнится без транзакции.\s
            
            Такой режим полезен, когда операция может работать как в транзакции, так и вне её, без особых требований к атомарности.
        """
)
@RestController
@RequestMapping("/transactions/supports")
public class SupportsTransactionController {
    private final SupportsPropagationService supportsService;

    public SupportsTransactionController(SupportsPropagationService supportsService) {
        this.supportsService = supportsService;
    }

    /**
     * Демонстрация поведения Propagation.SUPPORTS.
     *
     * @param userId ID пользователя, для которого запрашиваются транзакции
     * @return список логов транзакций пользователя
     */
    @Operation(
            summary = "Демонстрация Propagation.SUPPORTS: выполнение с или без транзакции",
            description = """
                    Этот метод демонстрирует поведение Propagation.SUPPORTS.\s
                    Если вызывается внутри транзакции — выполнится в её рамках.\s
                    Если вызывается вне транзакции — выполнится без транзакции.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транзакции успешно получены"),
            @ApiResponse(responseCode = "500", description = "Ошибка при выполнении операции")
    })
    @GetMapping("/{userId}")
    public List<TransactionLog> testSupports(
            @Parameter(description = "ID пользователя, для которого запрашиваются транзакции")
            @PathVariable Long userId
    ) {
        return supportsService.getUserTransactions(userId);
    }
}

