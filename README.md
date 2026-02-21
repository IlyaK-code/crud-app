# 🛍️ Product CRUD API

Spring Boot 3.x REST API для управления товарами с расчётом скидок.  
Тестовое задание для позиции Java-разработчика.

---

## 🔧 Технологии

| Компонент              | Версия  | Назначение                            |
|------------------------|---------|---------------------------------------|
| **Java**               | 21      | Язык программирования                 |
| **Spring Boot**        | 3.5.11  | Фреймворк для создания веб-приложений |
| **Spring Data JPA**    | 3.5.9   | Работа с БД через JPQL                |
| **PostgreSQL**         | 17      | Реляционная база данных               |
| **Maven**              | 3.9.9   | Сборка и управление зависимостями     |
| **Lombok**             | 1.18.42 | Снижение бойлерплейта                 |
| **Jakarta Validation** | 3.0.2   | Валидация входных данных              |
| **Swagger UI**         | 2.7     | документация к API                    |

---

## 📝 Описание API и примеры запросов/ответов

1. **POST** `/products` - создание товара

#### Пример запроса:

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Effective Java",
    "description": "Best practices",
    "price": 2500.00,
    "category": "BOOKS"
  }'
```

#### Пример ответа:

```json
{
  "id": 1,
  "name": "Effective Java",
  "description": "Best practices",
  "price": 2500.00,
  "discountedPrice": 2250.00,
  "category": "BOOKS",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Далее будут примеры на основе запроса на созданный товар

#### (представим что у нас в БД только 1 товар, который мы создали)

2. **GET**  `/products/id` - получение товара по ID

#### Пример запроса:

```bash
curl http://localhost:8080/products/1
```

#### Пример ответа:

```json
{
  "id": 1,
  "name": "Effective Java",
  "description": "Best practices",
  "price": 2500.00,
  "discountedPrice": 2250.00,
  "category": "BOOKS",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

3. **GET**  `/products?page=&size=&category=` - получние всех товаров (можно отфильтровать по категории)

#### Пример запроса:

```bash
curl "http://localhost:8080/products?page=0&size=10&category=BOOKS"
```

| Параметр   | Тип     | Обязательный | Значение по умолчанию | Описание                                        |
|------------|---------|--------------|-----------------------|-------------------------------------------------|
| `page`     | integer | нет          | `0`                   | Номер страницы (0-based)                        |
| `size`     | integer | нет          | `10`                  | Элементов на странице                           |
| `category` | string  | нет          | —                     | Фильтр: `ELECTRONICS`, `BOOKS`, `FOOD`, `OTHER` |

#### Пример ответа:
```json
{
  "content": [
    {
      "id": 1,
      "name": "Effective Java",
      "description": "Best practices",
      "price": 2500.00,
      "discountedPrice": 2250.00,
      "category": "BOOKS",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "page": {
    "size": 10,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```
6. **DELETE** `/products/id` - удаление товара

#### Пример запроса:

```bash
curl -X DELETE http://localhost:8080/products/1
```

### Коды ответов API

| Код   | Описание              | Когда возвращается              |
|-------|-----------------------|---------------------------------|
| `200` | OK                    | Успешное чтение/обновление      |
| `201` | Created               | Успешное создание товара        |
| `204` | No Content            | Успешное удаление               |
| `400` | Bad Request           | Ошибка валидации входных данных |
| `404` | Not Found             | Товар с указанным ID не найден  |
| `500` | Internal Server Error | Внутренняя ошибка сервера       |

### Подробнее о запросах и ответах можно прочитать в [Swagger UI](http://localhost:8080/swagger-ui/index.html) после запуска проекта и там же их протестировать самостоятельно

---

## 🚀 Сборка и запуск

### 1. Клонирование репозитория

```bash
git clone https://github.com/IlyaK-code/crud-app.git
```

### 2. Перейти в директорию проекта

```bash
cd crud-app
```

### 3. Сборка проекта

```bash
mvn clean package
```

### 4. Запуск БД в Docker из файла docker-compose.yml

```bash
docker-compose up -d
```

### 5. Запуск приложения

```bash
mvn spring-boot:run
```