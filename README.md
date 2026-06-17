Project Overview

This project is a resilient event-driven user activity tracking system built using Spring Boot, Apache Kafka, and Docker.
The application exposes REST APIs to publish and consume user activity events asynchronously using Kafka topics.

The system demonstrates core Kafka concepts such as:

Kafka Producers
Kafka Consumers
Kafka Topics
Topic Partitioning
Consumer Groups
Dead Letter Topic (DLT)
Retry Handling
Event-driven Architecture
Tech Stack
Java 17
Spring Boot 3
Apache Kafka
Spring Kafka
Docker
Docker Compose
Maven
Architecture
Client Request
      ↓
REST API (Producer)
      ↓
Kafka Topic (user-activity-events)
      ↓
Kafka Consumer
      ↓
In-Memory Store (ConcurrentHashMap)
      ↓
GET APIs / Stats APIs

For invalid events:

Invalid Event
      ↓
Consumer Failure
      ↓
Retry Handling
      ↓
Dead Letter Topic (user-activity-events.DLT)
Kafka Topics
Topic Name	Purpose
user-activity-events	Main event topic
user-activity-events.DLT	Dead Letter Topic for failed events
Features
Produce user activity events to Kafka
Consume events asynchronously
Store events in memory
Retrieve user activity history
Retrieve aggregate statistics
Kafka partitioning using userId
Dead Letter Topic handling
Dockerized setup
Project Structure
src/
 ├── config/
 ├── consumer/
 ├── controller/
 ├── dto/
 ├── producer/
 ├── store/
 └── resources/
Setup Instructions
Clone Repository
git clone <your-github-repo-url>
cd user-activity-tracker-kafka/activitytracker
Run with Docker
docker-compose up --build
Verify Running Containers
docker ps
Verify Kafka Topics
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list

Expected topics:

user-activity-events
user-activity-events.DLT
API Endpoints
1. Publish Activity Event
Endpoint
POST /api/activity
Request Body
{
  "userId": "user-123",
  "action": "CLICK",
  "resourceId": "home-page",
  "timestamp": 1715850000
}
Response
{
  "status": "ACCEPTED",
  "eventId": "uuid-value"
}
2. Get User Activities
Endpoint
GET /api/activity/{userId}
Example
GET /api/activity/user-123
Response
[
  {
    "userId": "user-123",
    "action": "CLICK",
    "resourceId": "home-page",
    "timestamp": 1715850000
  }
]
3. Get Activity Statistics
Endpoint
GET /api/activity/stats
Response
{
  "totalEvents": 3,
  "eventsByAction": {
    "PAGE_VIEW": 1,
    "CLICK": 2
  }
}
Dead Letter Topic (DLT)

If an event contains a null userId, the consumer intentionally fails processing and routes the message to:

user-activity-events.DLT

This prevents poison pill messages from blocking the consumer pipeline.

Verify DLT Messages

Run:

docker exec -it kafka kafka-console-consumer --bootstrap-server kafka:29092 --topic user-activity-events.DLT --from-beginning

Send invalid request:

{
  "userId": null,
  "action": "CLICK",
  "resourceId": "bad-event",
  "timestamp": 1715850000
}

Expected output:

{"userId":null,"action":"CLICK","resourceId":"bad-event","timestamp":1715850000}
Partitioning Strategy

The userId is used as the Kafka partition key.

Benefits:

Preserves ordering for each user
Ensures all events for same user go to same partition
Supports scalable parallel processing
Testing

The application was tested using:

Postman
Kafka Console Consumer
Docker Containers
Manual API Testing
Environment Variables

.env.example

SERVER_PORT=8080
KAFKA_BROKERS=kafka:29092
Build Project
./mvnw clean install
Run Locally
./mvnw spring-boot:run
Future Improvements
Database persistence
Swagger/OpenAPI integration
Unit and integration testing
Authentication & Authorization
Kafka Streams analytics
Author
KALARI SRISUCHA