package com.zor07.transactional_demo.service.propagation.propagation.never;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    @Transactional(propagation = Propagation.NEVER)
    public void sendEmail(Long userId, String message) {
        System.out.println("Sending email to user " + userId + ": " + message);
    }
}

