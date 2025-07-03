# Spring Transactional Propagation Demo

## 📘 Описание

Этот проект демонстрирует поведение аннотации `@Transactional` с различными значениями параметра `propagation` в Spring Framework.  
Он содержит контроллеры, сервисы и репозитории, оформленные так, чтобы наглядно показать различия в поведении транзакций при использовании разных стратегий propagation.

---

## 🚀 Запуск проекта

1. Убедитесь, что установлен **Docker** и **Docker Compose**.

2. В корне проекта находится файл `docker-compose.yml`. Поднимите контейнер с базой данных командой:

   ```bash
   docker-compose up -d
   ```

3. Запустите Spring Boot приложение:

    * Откройте проект в вашей IDE (например, IntelliJ IDEA)
    * Найдите и запустите метод `main` в классе `TransactionalDemoApplication`

---

## 📚 Swagger UI

Swagger UI для взаимодействия с REST-эндпоинтами доступен по адресу:

http://localhost:8080/swagger-ui/index.html

---

## 🧪 Что исследуется

В проекте демонстрируются следующие значения propagation:

* `REQUIRED`
* `REQUIRES_NEW`
* `NESTED`
* `SUPPORTS`
* `NOT_SUPPORTED`
* `NEVER`
* `MANDATORY`

Каждому режиму соответствует отдельный пример, позволяющий понять его поведение при успешных операциях и при выбрасывании исключений.

---

## 🛠️ Стек технологий

* Java 17+
* Spring Boot
* Spring Data JPA
* PostgreSQL (в Docker)
* springdoc-openapi (Swagger UI)
* Liquibase (миграции БД)


