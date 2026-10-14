# 💳 Payment Processing System

A backend payment processing system built with **Spring Boot**, simulating core banking operations — user and account management, and secure money transfers between accounts with transactional integrity and concurrency protection.

Built as a hands-on project to go deep on real backend engineering concerns: transactional consistency, concurrent-update safety, layered architecture, and clean API design — not just CRUD.

---

## 🚀 Features

- **User Management** — create and retrieve users, each assigned a unique UPI-style identifier
- **Account Management** — create bank accounts linked to users, with account type (Savings/Current), balance tracking
- **Money Transfer** — transfer funds between accounts with:
  - Balance validation (rejects transfers that would overdraw an account)
  - **Atomic transactions** (`@Transactional`) — if any step fails, the entire transfer rolls back, so money is never deducted without being credited
  - **Optimistic locking** (`@Version`) — prevents lost updates when two transfers hit the same account concurrently
  - Full transaction audit log (every transfer, successful or failed, is recorded with a timestamp)
- **Clean layered architecture** — Controller → Service → Repository → Entity, with DTOs decoupling the API contract from the database schema

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Data Access | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Security | Spring Security (baseline config) |
| Build Tool | Maven |
| Testing | Postman (manual integration testing) |

---

## 🏗️ Architecture

```
Client (Postman / UI)
        │
        ▼
┌───────────────────┐
│   Controller       │  ← handles HTTP requests/responses, no business logic
└───────────────────┘
        │
        ▼
┌───────────────────┐
│    Service         │  ← business logic, validation, transaction orchestration
└───────────────────┘
        │
        ▼
┌───────────────────┐
│   Repository        │  ← Spring Data JPA interfaces, auto-implemented
└───────────────────┘
        │
        ▼
┌───────────────────┐
│  Entity / Database  │  ← Hibernate-mapped entities, PostgreSQL
└───────────────────┘
```

**Why DTOs?** Entities are never exposed directly through the API. Request/response DTOs decouple the public API contract from the internal database schema, and avoid leaking internal fields or triggering unintended lazy-loading issues.

**Why `@Transactional`?** The money transfer operation touches two accounts and writes a transaction log — all three must succeed together or not at all. `@Transactional` ensures atomicity: if any step throws an exception, every database change made in that method is rolled back.

**Why `@Version` (optimistic locking)?** Without it, two simultaneous transfers against the same account could both read the same starting balance and both succeed, silently corrupting the account balance (a lost update). `@Version` makes Hibernate detect concurrent modifications and reject the stale update instead of silently overwriting it.

---

## 📡 API Endpoints

### Users

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Create a new user |
| GET | `/api/users` | List all users |

**Sample request — `POST /api/users`**
```json
{
  "userName": "Aditi",
  "emailId": "aditi@example.com",
  "mobileNumber": "9876543210"
}
```

### Accounts

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/accounts` | Create a new account linked to a user |
| GET | `/api/accounts` | List all accounts |

**Sample request — `POST /api/accounts`**
```json
{
  "accountNumber": "ACC1001",
  "bankname": "HDFC",
  "accountType": "SAVINGS",
  "accountHolderName": "Aditi",
  "accountHolderAddress": "Pune",
  "upiId": "<user's upiId>"
}
```

### Transactions

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions/transfer` | Transfer money between two accounts |

**Sample request — `POST /api/transactions/transfer`**
```json
{
  "fromAccountNumber": "ACC1001",
  "toAccountNumber": "ACC1002",
  "amount": 500
}
```

**Sample response**
```json
{
  "transactionId": 1,
  "fromAccountNumber": "ACC1001",
  "toAccountNumber": "ACC1002",
  "amount": 500.0,
  "status": "SUCCESS",
  "remarks": "Transfer successful",
  "timestamp": "2026-10-05T11:36:34.294"
}
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven
- PostgreSQL running locally

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/payment-processing-system.git
   cd payment-processing-system
   ```

2. **Create the database**
   ```sql
   CREATE DATABASE payment_processing;
   ```

3. **Configure `application.properties`**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/payment_processing
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   App starts on `http://localhost:8081`

5. **Test with Postman** — import the sample requests above, or use the provided Postman collection (see `/postman` folder).

---

## 🧠 What I Learned Building This

- Designing entity relationships (`@OneToOne`) and resolving the natural-key vs surrogate-key tradeoff for primary keys
- Debugging real Hibernate/JPA schema generation issues (identity column types, DDL auto-update limitations)
- Implementing optimistic concurrency control and understanding when it's the right tool vs pessimistic locking
- Structuring a Spring Boot app with proper separation of concerns (Controller/Service/Repository/DTO)
- Using `@Transactional` correctly to guarantee atomicity across multi-step database operations

---

## 🔮 Planned Improvements

- [ ] Idempotency keys to prevent duplicate transfer processing on client retries
- [ ] JWT-based authentication with Spring Security
- [ ] Global exception handling via `@ControllerAdvice` for consistent error responses
- [ ] Transaction history endpoint with pagination (`Pageable`)
- [ ] Redis caching for account balance lookups
- [ ] Kafka event publishing on successful transfers
- [ ] Dockerized setup (`docker-compose` for app + Postgres)
- [ ] Integration tests with Testcontainers
- [ ] Frontend UI for interacting with the API

---


