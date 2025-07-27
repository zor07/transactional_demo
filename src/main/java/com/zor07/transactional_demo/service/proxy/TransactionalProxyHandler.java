package com.zor07.transactional_demo.service.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Обработчик прокси-вызовов, реализующий интерфейс {@link InvocationHandler}, с ручным управлением транзакциями.
 * <p>
 * Используется в учебном проекте для демонстрации того, как работает аннотация {@code @Transactional} "под капотом"
 * в Spring. Вместо использования AOP и аннотаций, транзакция создаётся, коммитится и откатывается вручную.
 * <p>
 * Каждый вызов метода через прокси будет оборачиваться в следующую логику:
 * <ol>
 *     <li>Начать транзакцию через {@link PlatformTransactionManager#getTransaction}.</li>
 *     <li>Вызвать метод оригинального объекта (реализации сервиса).</li>
 *     <li>Если метод отработал без исключений — выполнить коммит транзакции.</li>
 *     <li>Если возникло исключение — откатить транзакцию.</li>
 * </ol>
 * <p>
 * Это позволяет увидеть реальный механизм управления транзакцией, на который полагается Spring, но в явной форме.
 *
 * <p><b>Примечание:</b> такой способ работает только с интерфейсами, потому что используется JDK Proxy
 * (а не CGLIB, который может проксировать и классы).</p>
 *
 * <p><b>Пример использования:</b> см. {@code @Bean proxyDemoTransactionService()} — бин вручную создаётся через этот прокси.</p>
 */
public class TransactionalProxyHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(TransactionalProxyHandler.class);

    /**
     * Целевой объект, на который делегируются вызовы методов.
     */
    private final Object target;
    /**
     * Менеджер транзакций Spring, через который управляем транзакциями вручную.
     */
    private final PlatformTransactionManager transactionManager;

    public TransactionalProxyHandler(Object target, PlatformTransactionManager transactionManager) {
        this.target = target;
        this.transactionManager = transactionManager;
    }


    /**
     * Перехватывает вызовы методов и оборачивает их в транзакцию.
     *
     * <p>Если метод выполнится успешно — транзакция будет закоммичена.
     * В случае ошибки — произойдёт rollback.</p>
     *
     * @param proxy Прокси-объект (не используется).
     * @param method Метод, который вызывается.
     * @param args Аргументы метода.
     * @return Результат выполнения метода.
     * @throws Throwable Любая ошибка, возникшая в процессе выполнения метода.
     */
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
