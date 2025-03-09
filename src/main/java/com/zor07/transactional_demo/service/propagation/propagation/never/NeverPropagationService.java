package com.zor07.transactional_demo.service.propagation.propagation.never;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NeverPropagationService {

    private final NotificationService notificationService;

    public NeverPropagationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Transactional
    public void processWithTransaction(Long userId, String message) {
        notificationService.sendEmail(userId, message); // вызовет исключение

    }

    public void processWithoutTransaction(Long userId, String message) {
        notificationService.sendEmail(userId, message); // выполнится успешно
    }

}