package com.zor07.transactional_demo.service.propagation.proxy;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionalProxyHandler implements InvocationHandler {
    private final Object target;
    private final PlatformTransactionManager transactionManager;

    public TransactionalProxyHandler(Object target, PlatformTransactionManager transactionManager) {
        this.target = target;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        System.out.println("🔹 [Транзакция началась]");

        try {
            Object result = method.invoke(target, args);
            transactionManager.commit(status);
            System.out.println("✅ [Транзакция успешно завершена]");
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            System.out.println("❌ [Ошибка, транзакция откатывается]");
            throw e;
        }
    }

    public static <T> T createProxy(T target, Class<T> interfaceType, PlatformTransactionManager transactionManager) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[]{interfaceType},
                new TransactionalProxyHandler(target, transactionManager)
        );
    }
}
