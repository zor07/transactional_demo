package com.zor07.transactional_demo.service.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TransactionalProxyHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(TransactionalProxyHandler.class);

    private final Object target;
    private final PlatformTransactionManager transactionManager;

    public TransactionalProxyHandler(Object target, PlatformTransactionManager transactionManager) {
        this.target = target;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("🔹 [Транзакция началась]");

        try {
            Object result = method.invoke(target, args);
            transactionManager.commit(status);
            log.info("✅ [Транзакция успешно завершена]");
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.info("❌ [Ошибка, транзакция откатывается]");
            throw e;
        }
    }
}
