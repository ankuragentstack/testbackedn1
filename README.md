# testbackedn1

A Spring Boot Java backend that returns the current time for a given IANA timezone.

## Endpoint

```
GET /config/v1/testTime/{timezone}
```

| Parameter  | Location | Description                            |
|------------|----------|----------------------------------------|
| `timezone` | URL path | IANA timezone ID, e.g. `Asia/Kolkata`  |

### Success response `200 OK`

```json
{
  "timezone": "America/New_York",
  "currentTime": "2026-04-26T07:46:42-04:00",
  "utcOffset": "-04:00"
}
```

### Error response `400 Bad Request`

```json
{
  "error": "Unknown timezone: BadZone",
  "hint": "Use IANA zone IDs such as America/New_York or Asia/Kolkata"
}
```

## Run locally

Requires Java 17+. No Maven installation needed — the wrapper is included.

```bash
./mvnw spring-boot:run
```

Server starts on **port 8080**.

### Example requests

```bash
curl http://localhost:8080/config/v1/testTime/UTC
curl http://localhost:8080/config/v1/testTime/Asia/Kolkata
curl http://localhost:8080/config/v1/testTime/America/New_York
curl http://localhost:8080/config/v1/testTime/Europe/London
```

## Build a JAR

```bash
./mvnw package -q
java -jar target/testbackedn1-1.0.0.jar
```

## Project structure

```
src/main/java/com/testbackend/
├── Application.java              # Spring Boot entry point
└── controller/
    └── TimeController.java       # GET /config/v1/testTime/**
```
