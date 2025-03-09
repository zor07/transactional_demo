package com.zor07.transactional_demo.controller;

import com.zor07.transactional_demo.entity.TransactionLog;
import com.zor07.transactional_demo.service.propagation.propagation.mandatory.MandatoryPropagationService;
import com.zor07.transactional_demo.service.propagation.propagation.nested.NestedPropagationService;
import com.zor07.transactional_demo.service.propagation.propagation.never.NeverPropagationService;
import com.zor07.transactional_demo.service.propagation.propagation.not_supported.NotSupportedPropagationService;
import com.zor07.transactional_demo.service.propagation.propagation.required.RequiredPropagationService;
import com.zor07.transactional_demo.service.propagation.propagation.requires_new.RequiresNewPropagationService;
import com.zor07.transactional_demo.service.propagation.propagation.supports.SupportsPropagationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Транзакции (Propagation)", description = "Демонстрация различных стратегий propagation в Spring")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final NestedPropagationService nestedService;
    private final MandatoryPropagationService mandatoryService;
    private final SupportsPropagationService supportsService;
    private final NotSupportedPropagationService notSupportedService;
    private final NeverPropagationService neverService;
    private final RequiredPropagationService requiredService;
    private final RequiresNewPropagationService requiresNewService;

    public TransactionController(NestedPropagationService nestedService,
                                 MandatoryPropagationService mandatoryService,
                                 SupportsPropagationService supportsService,
                                 NotSupportedPropagationService notSupportedService,
                                 NeverPropagationService neverService,
                                 RequiredPropagationService requiredService,
                                 RequiresNewPropagationService requiresNewService) {
        this.nestedService = nestedService;
        this.mandatoryService = mandatoryService;
        this.supportsService = supportsService;
        this.notSupportedService = notSupportedService;
        this.neverService = neverService;
        this.requiredService = requiredService;
        this.requiresNewService = requiresNewService;
    }

    @Operation(summary = "Тестирование NESTED транзакции")
    @PostMapping("/nested/{userId}")
    public void testNested(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        nestedService.processTransaction(userId, amount);
    }

    @Operation(summary = "Тестирование MANDATORY транзакции")
    @PostMapping("/mandatory/{userId}")
    public void testMandatory(@PathVariable Long userId,
                              @RequestParam BigDecimal amount,
                              @RequestParam(required = false, defaultValue = "false") boolean withTransaction) {
        if (withTransaction) {
            mandatoryService.processWithTransaction(userId, amount);
        } else {
            mandatoryService.processWithoutTransaction(userId, amount);
        }
    }

    @Operation(summary = "Тестирование SUPPORTS транзакции")
    @GetMapping("/supports/{userId}")
    public List<TransactionLog> testSupports(@PathVariable Long userId) {
        return supportsService.getUserTransactions(userId);
    }

    @Operation(summary = "Тестирование NOT_SUPPORTED транзакции")
    @PostMapping("/not-supported/{userId}")
    public void testNotSupported(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        notSupportedService.processTransaction(userId, amount);
    }

    @Operation(summary = "Тестирование NEVER транзакции")
    @PostMapping("/never/{userId}")
    public void testNever(@PathVariable Long userId,
                          @RequestParam String message,
                          @RequestParam(required = false, defaultValue = "false") boolean withTransaction) {
        if (withTransaction) {
            neverService.processWithTransaction(userId, message);
        } else {
            neverService.processWithoutTransaction(userId, message);
        }
    }

    @Operation(summary = "Тестирование REQUIRED транзакции")
    @PostMapping("/required/{userId}")
    public void testRequired(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        requiredService.processTransaction(userId, amount);
    }

    @Operation(summary = "Тестирование REQUIRES_NEW транзакции")
    @PostMapping("/requires-new/{userId}")
    public void testRequiresNew(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        requiresNewService.processTransaction(userId, amount);
    }
}

