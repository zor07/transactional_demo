package com.zor07.transactional_demo.controller.propagation;

import com.zor07.transactional_demo.service.propagation.never.NeverPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Propagation.NEVER",
        description = """
            Propagation.NEVER означает, что метод **не должен** вызываться внутри транзакции.\s
            Если активная транзакция существует, Spring выбрасывает исключение IllegalTransactionStateException.\s
            Если транзакции нет — метод выполняется нормально.
            
            Этот режим полезен, когда операция не должна быть частью транзакционной логики.
        """
)
@RestController
@RequestMapping("/transactions/never")
public class NeverTransactionController {
    private final NeverPropagationService neverService;

    public NeverTransactionController(NeverPropagationService neverService) {
        this.neverService = neverService;
    }

    /**
     * Демонстрация поведения Propagation.NEVER.
     *
     * @param userId ID пользователя
     * @param message Сообщение для обработки
     * @param withTransaction Если true — вызывается метод с активной транзакцией (будет ошибка).\s
     *                        Если false — вызывается без транзакции (выполнится успешно).
     */
    @Operation(
            summary = "Демонстрация Propagation.NEVER: запрещено вызывать внутри транзакции",
            description = """
                    Этот метод демонстрирует поведение Propagation.NEVER.\s
                    При withTransaction=true — метод вызывается внутри транзакции и выбрасывает ошибку.\s
                    При withTransaction=false — метод вызывается вне транзакции и работает нормально.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "500", description = "Ошибка: вызов внутри транзакции запрещён (NEVER)")
    })
    @PostMapping("/{userId}")
    public void testNever(
            @Parameter(description = "ID пользователя")
            @PathVariable Long userId,

            @Parameter(description = "Сообщение для обработки")
            @RequestParam String message,

            @Parameter(description = """
                true — метод вызывается внутри транзакции (ошибка).
                false — метод вызывается без транзакции (успех).
                """)
            @RequestParam(required = false, defaultValue = "false") boolean withTransaction
    ) {
        if (withTransaction) {
            neverService.processWithTransaction(userId, message);
        } else {
            neverService.processWithoutTransaction(userId, message);
        }
    }
}

