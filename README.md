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
4. Swagger UI будет доступен по адресу: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Тестовые данные

При первом запуске будут добавлены тестовые споты, инструкторы, типы спотов и фото (см. `src/main/resources/data.sql`).

## Валидация данных

### Обязательные поля
- **spotTypeId** - обязательное поле при создании и обновлении спота
- Все основные поля спота (name, description, city, address, price, schedule, latitude, longitude) являются обязательными

### Констрейнты базы данных
- Тип спота должен существовать в таблице `spot_types`
- Названия типов спотов уникальны
- Цена спота должна быть положительной
- Координаты должны быть в допустимых диапазонах (широта: -90 до 90, долгота: -180 до 180)
- Длины строк ограничены (название: 1-255 символов, описание: 1-1000 символов)

## API Эндпоинты

### Споты (Spots)

- **Получить все споты:**
  ```bash
  curl http://localhost:8080/api/spots
  ```

- **Поиск спотов с фильтрацией:**
  ```bash
  curl "http://localhost:8080/api/spots?query=Москва&spotTypeId=1"
  ```

- **Получить спот по ID:**
  ```bash
  curl http://localhost:8080/api/spots/1
  ```

- **Создать новый спот:**
  ```bash
  curl -X POST http://localhost:8080/api/spots \
    -H 'Content-Type: application/json' \
    -d '{
      "name": "Новый спот",
      "description": "Описание спота",
      "city": "Москва",
      "address": "ул. Примерная, 1",
      "price": 500,
      "schedule": "Пн-Вс 8:00-22:00",
      "latitude": 55.751244,
      "longitude": 37.618423,
      "spotTypeId": 1,
      "mainPhotoUrl": "https://example.com/photo.jpg"
    }'
  ```

- **Обновить спот (spotTypeId обязателен):**
  ```bash
  curl -X PUT http://localhost:8080/api/spots/1 \
    -H 'Content-Type: application/json' \
    -d '{
      "name": "Обновленное название",
      "price": 600,
      "spotTypeId": 1
    }'
  ```

- **Удалить спот:**
  ```bash
  curl -X DELETE http://localhost:8080/api/spots/1
  ```

- **Получить споты по типу:**
  ```bash
  curl http://localhost:8080/api/spots/by-type/1
  ```

### Типы спотов (Spot Types)

- **Получить все типы спотов:**
  ```bash
  curl http://localhost:8080/api/spot-types
  ```

- **Получить тип спота по ID:**
  ```bash
  curl http://localhost:8080/api/spot-types/1
  ```

- **Создать новый тип спота:**
  ```bash
  curl -X POST http://localhost:8080/api/spot-types \
    -H 'Content-Type: application/json' \
    -d '{
      "name": "Новый тип",
      "description": "Описание типа"
    }'
  ```

- **Поиск типов спотов:**
  ```bash
  curl "http://localhost:8080/api/spot-types/search?name=скейт"
  ```

### Фото спотов (Spot Photos)

- **Получить все фото спота:**
  ```bash
  curl http://localhost:8080/api/spots/1/photos
  ```

- **Получить главное фото спота:**
  ```bash
  curl http://localhost:8080/api/spots/1/photos/main
  ```

- **Добавить фото к споту:**
  ```bash
  curl -X POST http://localhost:8080/api/spots/1/photos \
    -H 'Content-Type: application/json' \
    -d '{
      "photoUrl": "https://example.com/new-photo.jpg",
      "isMain": false
    }'
  ```

- **Установить главное фото:**
  ```bash
  curl -X PUT http://localhost:8080/api/spots/1/photos/2/main
  ```

- **Удалить фото:**
  ```bash
  curl -X DELETE http://localhost:8080/api/spots/1/photos/2
  ```

### Инструкторы (Instructors)

- **Получить карточку инструктора:**
  ```bash
  curl http://localhost:8080/api/instructors/1
  ```

- **Создать инструктора:**
  ```bash
  curl -X POST http://localhost:8080/api/instructors \
    -H 'Content-Type: application/json' \
    -d '{"name":"Иван","specialization":"Йога","experience":5,"description":"Опытный тренер","price":1000,"contacts":"+79998887766"}'
  ```

## Документация

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

Если возникнут вопросы — пиши! 