# 📦 Order Notification Service

A microservice for processing order events using **Apache Kafka** and **PostgreSQL**.

## 📋 Table of Contents

- [About](#-about)
- [How it Works](#-how-it-works)
- [Technologies](#-technologies)
- [Requirements](#-requirements)
- [How to Run Locally](#-how-to-run-locally)
- [Testing the API](#-testing-the-api)
- [Monitoring](#-monitoring)
- [Running Tests](#-running-tests)
- [Project Structure](#-project-structure)

---

## 📖 About

This application receives HTTP requests with order information, validates the data, and sends events to **Apache Kafka**. Then it:
- Saves events to **PostgreSQL** database
- Sends email notifications (mock implementation)
- Processes everything asynchronously using Kafka Consumer

### Main features:
✅ REST API for receiving orders  
✅ Input validation (email format, country codes ISO 3166-1 alpha-2)  
✅ Asynchronous processing with Apache Kafka  
✅ Data persistence in PostgreSQL (migrations with Flyway)  
✅ Mock email service  
✅ Kafka UI for monitoring  
✅ Health checks and metrics (Spring Actuator)

---

## 🏗️ How it Works

**Flow:**

1. Client sends POST request to `/api/orders`
2. Controller validates the request data
3. OrderService sends event to Kafka topic `order-events`
4. Kafka stores the message
5. OrderConsumerService receives the event from Kafka
6. Consumer saves event to PostgreSQL database
7. Consumer sends email notification (mock)

**Components:**
- **OrderController** - receives HTTP requests
- **OrderService** - produces events to Kafka
- **OrderConsumerService** - consumes events from Kafka
- **EmailService** - sends notifications (mock)
- **PostgreSQL** - stores order events
- **Kafka** - message broker (3 partitions)

---

## 🛠️ Technologies

**Backend:**
- Java 17
- Spring Boot 3.4.2
- Spring Kafka
- Spring Data JPA
- Flyway (database migrations)

**Infrastructure:**
- Apache Kafka 3.8.1
- PostgreSQL 15
- Docker Compose

**Testing:**
- JUnit 5
- Mockito
- Spring Boot Test

**Build Tools:**
- Maven 3.9+

---

## ⚙️ Requirements

- **Java 17+** ([download OpenJDK](https://adoptium.net/))
- **Docker Desktop** ([download](https://www.docker.com/products/docker-desktop))
- **Maven 3.9+** (or use Maven Wrapper included in project)
- **Git**

---

## 🚀 How to Run Locally

### 1. Clone the repository

```bash
git clone https://github.com/PeKowalkowskia/recruitment-task.git
cd recruitment-task
```

### 2. Start infrastructure with Docker Compose

```bash
docker-compose up -d
```

This will start:
- **PostgreSQL** (port 5432)
- **Zookeeper** (port 2181)
- **Kafka** (port 9092)
- **Kafka UI** (port 8090)

Check status:
```bash
docker-compose ps
```

You should see all services running (healthy).

### 3. Start the application

#### Option A: Maven Wrapper (Windows)
```powershell
.\mvnw.cmd spring-boot:run
```

#### Option B: Maven Wrapper (Linux/Mac)
```bash
./mvnw spring-boot:run
```

#### Option C: IntelliJ IDEA
1. Open project in IntelliJ
2. Find `RecruitmentTaskApplication` class
3. Click the green ▶️ button

### 4. Verify it's working

The application should be available at: **http://localhost:8080**

Check health:
```bash
curl http://localhost:8080/actuator/health
```

You should see: `{"status":"UP"}`

---

## 🧪 Testing the API

### Endpoint: **POST /api/orders**

#### Example request (cURL - Linux/Mac):

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "trackingNumber": "TEST123",
    "recipientEmail": "customer@example.com",
    "recipientCountryCode": "PL",
    "senderCountryCode": "DE",
    "statusCode": 50
  }'
```

#### PowerShell (Windows):

```powershell
$body = @{
  trackingNumber = "TEST123"
  recipientEmail = "customer@example.com"
  recipientCountryCode = "PL"
  senderCountryCode = "DE"
  statusCode = 50
} | ConvertTo-Json

Invoke-RestMethod -Uri http://localhost:8080/api/orders -Method POST -Body $body -ContentType "application/json"
```

#### Response (HTTP 202 Accepted):

```json
{
  "trackingNumber": "TEST123",
  "recipientEmail": "customer@example.com",
  "recipientCountryCode": "PL",
  "senderCountryCode": "DE",
  "statusCode": 50,
  "eventTimestamp": "2026-02-14T10:30:00.123456"
}
```

### Validation rules

**Required fields:**
- `trackingNumber` - cannot be empty
- `recipientEmail` - must be valid email format
- `recipientCountryCode` - must be 2 characters (e.g., "PL", "DE", "US")
- `senderCountryCode` - must be 2 characters
- `statusCode` - must be a number

#### Example validation error (HTTP 400):

```json
{
  "timestamp": "2026-02-14T10:30:00.123",
  "status": 400,
  "error": "Bad Request",
  "message": "recipientEmail: must be a well-formed email address"
}
```

---

## 🔍 Monitoring

### Kafka UI

Open in browser: **http://localhost:8090**

You can:
- Browse topics
- See messages in `order-events` topic
- Monitor consumers (`order-processors` group)

### Spring Actuator

- **Health:** http://localhost:8080/actuator/health
- **Metrics:** http://localhost:8080/actuator/metrics
- **Info:** http://localhost:8080/actuator/info

### Application logs

The application logs all important events:

```
INFO - Processing order request for tracking number: TEST123
INFO - Order event sent to Kafka: tracking=TEST123
INFO - Received order event from Kafka: tracking=TEST123, status=50
INFO - [MOCK EMAIL] Sending notification to: customer@example.com
      Subject: Order Status Update - TEST123
      Status Code: 50
INFO - Order event saved to database: id=1
```

---

## 🧪 Running Tests

```bash
# Maven Wrapper (Windows)
.\mvnw.cmd test

# Maven Wrapper (Linux/Mac)
./mvnw test

# IntelliJ IDEA
# Right-click on src/test/java folder → Run 'All Tests'
```

Test coverage includes:
- `OrderServiceTest` - Kafka producer tests
- `OrderConsumerServiceTest` - Kafka consumer tests
- `OrderControllerTest` - API validation tests
- `EmailServiceTest` - Email service tests

---

## 📁 Project Structure

**Main source code:**
- `controller/` - REST API endpoints
- `service/` - business logic (Kafka producer/consumer, email)
- `domain/` - database entities (JPA)
- `dto/` - data transfer objects
- `repository/` - database access (Spring Data)

**Resources:**
- `application.yml` - local configuration
- `application-prod.yml` - production configuration
- `db/migration/` - database migrations (Flyway)

**Tests:**
- `test/java/.../service/` - unit tests with Mockito

---

## 🐳 Docker Compose Services

The `docker-compose.yml` file includes:

- **postgres** - PostgreSQL database
- **zookeeper** - Required by Kafka
- **kafka** - Apache Kafka broker
- **kafka-ui** - Web UI for Kafka monitoring

---

