# SportSocial Backend

Backend для мобильного приложения "СпортСоцСеть" на Kotlin + Spring Boot + PostgreSQL.

## Запуск

1. Установите PostgreSQL и создайте БД `sportsocial`:
   ```
   createdb sportsocial
   ```
2. Проверьте настройки подключения в `src/main/resources/application.yml`.
3. Соберите и запустите проект:
   ```
   ./gradlew bootRun
   ```
4. Swagger UI будет доступен по адресу: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Тестовые данные

При первом запуске будут добавлены тестовые споты и инструкторы (см. `src/main/resources/data.sql`).

## Примеры API

- Получить все споты:
  ```bash
  curl http://localhost:8080/api/spots
  ```
- Поиск по городу/названию:
  ```bash
  curl http://localhost:8080/api/spots?query=Москва
  ```
- Получить карточку спота:
  ```bash
  curl http://localhost:8080/api/spots/1
  ```
- Получить карточку инструктора:
  ```bash
  curl http://localhost:8080/api/instructors/1
  ```
- Создать инструктора:
  ```bash
  curl -X POST http://localhost:8080/api/instructors \
    -H 'Content-Type: application/json' \
    -d '{"name":"Иван","specialization":"Йога","experience":5,"description":"Опытный тренер","price":1000,"contacts":"+79998887766"}'
  ```

## Документация

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

Если возникнут вопросы — пиши! 