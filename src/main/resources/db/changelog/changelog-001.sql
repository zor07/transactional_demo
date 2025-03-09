-- Создание таблицы пользователей
CREATE TABLE users
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(100)   NOT NULL,
    balance NUMERIC(15, 2) NOT NULL DEFAULT 0
);

-- Создание таблицы логов транзакций
CREATE TABLE transaction_log
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    amount     NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
