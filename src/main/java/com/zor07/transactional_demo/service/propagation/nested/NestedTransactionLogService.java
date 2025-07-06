package com.zor07.transactional_demo.service.propagation.nested;

import com.zor07.transactional_demo.entity.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
public class NestedTransactionLogService {

    private final DataSource dataSource;
    private final PlatformTransactionManager txManager;

    public NestedTransactionLogService(DataSource dataSource, PlatformTransactionManager txManager) {
        this.dataSource = dataSource;
        this.txManager = txManager;
    }

    /**
     * Логирует транзакцию пользователя во вложенной транзакции с использованием savepoint'а.
     *
     * <p><b>Что такое savepoint?</b><br>
     * Savepoint — это контрольная точка внутри транзакции. Она позволяет откатить изменения,
     * выполненные после этой точки, не затрагивая остальную часть транзакции. Это полезно,
     * когда нужно "локально" обработать ошибку и продолжить выполнение основной транзакции.</p>
     *
     * <p><b>Пример:</b><br>
     * Если внешняя транзакция обновляет баланс пользователя, а внутренняя (вложенная)
     * сохраняет информацию о транзакции, то ошибка внутри внутренней логики
     * (например, при сохранении лога) не должна отменять обновление баланса. В этом случае
     * вложенная транзакция откатывается до savepoint'а, но внешняя продолжает выполнение.</p>
     *
     *  <pre><code>
     * START TRANSACTION<br>
     * |<br>
     * |-- действие A (обновили баланс)<br>
     * |-- SAVEPOINT s1<br>
     *     |<br>
     *     |-- действие B (записали лог)<br>
     *     |   → 💥 Ошибка<br>
     *     |-- ROLLBACK TO s1<br>
     * |<br>
     * |-- действие C<br>
     * COMMIT<br>
     * <pre><code>
     * <p><b>Почему здесь используется JDBC, а не JPA?</b><br>
     * JPA (Hibernate) по умолчанию не поддерживает Propagation.NESTED, потому что
     * {@code JpaTransactionManager} не работает с savepoint'ами. Вместо этого мы используем
     * {@code DataSourceTransactionManager}, который напрямую управляет соединением с БД и
     * поддерживает вложенные транзакции через savepoint API. Чтобы не смешивать JPA и JDBC в одном
     * transaction manager'е, здесь используется ручное управление транзакциями на уровне JDBC.</p>
     *
     * <p><b>Важно:</b> метод сам управляет транзакцией — не нужно (и нельзя) аннотировать его
     * {@code @Transactional}, иначе Spring возьмёт другой (возможно JPA) менеджер и поведение изменится.</p>
     *
     * @param user пользователь, для которого логируется операция
     * @param amount сумма операции
     * @param shouldFail если true — симулируется ошибка после вставки, чтобы проверить поведение savepoint
     */
    public void logTransaction(User user, BigDecimal amount, boolean shouldFail) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);

        TransactionStatus status = txManager.getTransaction(def);
        try (Connection conn = DataSourceUtils.getConnection(dataSource);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO transaction_log (user_id, amount, created_at) VALUES (?, ?, NOW())")) {

            ps.setLong(1, user.getId());
            ps.setBigDecimal(2, amount);
            ps.executeUpdate();

            if (shouldFail) {
                throw new RuntimeException("Simulated nested failure");
            }

            txManager.commit(status);
        } catch (Exception ex) {
            txManager.rollback(status);
            // Не пробрасываем — чтобы внешняя транзакция продолжилась
        }
    }
}