# User Activity Tracker Kafka

## Project Overview

User Activity Tracker Kafka is a resilient event-driven backend application built using Spring Boot, Apache Kafka, and Docker.

The system allows clients to publish user activity events through REST APIs, process them asynchronously using Kafka consumers, and retrieve user activity history and statistics through API endpoints.

The project demonstrates practical Kafka concepts such as event-driven architecture, partitioning, consumer groups, retry handling, and Dead Letter Topics (DLT).

---

## Key Features

* Publish user activity events asynchronously using Kafka
* Consume and process events through Kafka consumers
* Thread-safe in-memory event storage
* Event traceability using unique UUID-based event IDs
* Optimized statistics retrieval using AtomicLong counters
* Kafka partitioning using userId as the message key
* Dead Letter Topic (DLT) handling for failed events
* Retry mechanism for transient failures
* Structured application logging using SLF4J
* Explicit Kafka listener container configuration
* Dockerized deployment setup

---

## Technology Stack

* Java 17
* Spring Boot 3
* Apache Kafka
* Spring Kafka
* Docker
* Docker Compose
* Maven
* Lombok

---

## System Architecture

Client Request
↓
REST API (Producer)
↓
Kafka Topic (user-activity-events)
↓
Kafka Consumer
↓
Thread-Safe In-Memory Store
↓
GET APIs / Statistics APIs

### Failure Flow

Invalid Event
↓
Consumer Failure
↓
Retry Attempts
↓
Dead Letter Topic (user-activity-events.DLT)

---

## Kafka Topics

| Topic Name               | Purpose                             |
| ------------------------ | ----------------------------------- |
| user-activity-events     | Main event topic                    |
| user-activity-events.DLT | Dead Letter Topic for failed events |

---

## Project Structure

```text
src/
├── config/
├── consumer/
├── controller/
├── dto/
├── producer/
├── store/
└── resources/
```

---

## Setup Instructions

### Clone Repository

```bash
git clone <your-repository-url>
cd activitytracker
```

### Run with Docker

```bash
docker-compose up --build
```

### Verify Running Containers

```bash
docker ps
```

### Verify Kafka Topics

```bash
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list
```

Expected Topics:

```text
user-activity-events
user-activity-events.DLT
```

---

## API Endpoints

### 1. Publish Activity Event

**Endpoint**

```http
POST /api/activity
```

**Request Body**

```json
{
  "userId": "user-123",
  "action": "CLICK",
  "resourceId": "home-page",
  "timestamp": 1715850000
}
```

**Response**

```json
{
  "status": "ACCEPTED",
  "eventId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 2. Get User Activities

**Endpoint**

```http
GET /api/activity/{userId}
```

**Example**

```http
GET /api/activity/user-123
```

**Response**

```json
[
  {
    "eventId": "550e8400-e29b-41d4-a716-446655440000",
    "userId": "user-123",
    "action": "CLICK",
    "resourceId": "home-page",
    "timestamp": 1715850000
  }
]
```

---

### 3. Get Activity Statistics

**Endpoint**

```http
GET /api/activity/stats
```

**Response**

```json
{
  "totalEvents": 3,
  "eventsByAction": {
    "PAGE_VIEW": 1,
    "CLICK": 2
  }
}
```

---

## Dead Letter Topic (DLT)

Events with invalid data are retried before being redirected to the Dead Letter Topic.

Example invalid event:

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "userId": null,
  "action": "CLICK",
  "resourceId": "bad-event",
  "timestamp": 1715850000
}
```

This prevents poison-pill messages from blocking the consumer pipeline.

### Verify DLT Messages

```bash
docker exec -it kafka kafka-console-consumer \
--bootstrap-server kafka:29092 \
--topic user-activity-events.DLT \
--from-beginning
```

---

## Partitioning Strategy

The application uses **userId** as the Kafka partition key.

### Benefits

* Preserves ordering for each user
* Routes events from the same user to the same partition
* Enables scalable parallel processing
* Improves consumer performance

---

## Thread Safety and Performance Optimizations

### Thread-Safe Storage

Events are stored using:

* ConcurrentHashMap
* Synchronized Lists

This ensures safe access when multiple Kafka consumer threads process events concurrently.

### Optimized Statistics

Statistics are maintained using:

* AtomicLong
* ConcurrentHashMap

This enables O(1) statistics retrieval without scanning all stored events.

---

## Event Traceability

Every published event receives a unique UUID.

The same eventId is:

1. Returned to the client
2. Published to Kafka
3. Consumed by Kafka consumers
4. Stored in memory
5. Available through retrieval APIs

This makes event tracking and debugging easier.

---

## Logging

The application uses SLF4J-based structured logging instead of console output, providing production-ready logging support.

---

## Testing

The application was tested using:

* Postman
* Kafka Console Consumer
* Docker Containers
* Manual API Testing

---

## Environment Variables

```env
SERVER_PORT=8080
KAFKA_BROKERS=kafka:29092
```

---

## Build Project

```bash
./mvnw clean install
```

---

## Run Locally

```bash
./mvnw spring-boot:run
```

---

## Future Enhancements

* PostgreSQL persistence
* Swagger / OpenAPI documentation
* Integration testing
* Authentication and Authorization
* Kafka Streams analytics
* Monitoring and metrics

---

## Author

Kalari Srisucha
