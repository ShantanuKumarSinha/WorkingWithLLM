# WorkingWithLLM

A Spring Boot application that integrates with Large Language Models (LLMs) using [Spring AI](https://docs.spring.io/spring-ai/reference/).

## Features

- Simple chat endpoint – send a message, receive an LLM reply
- Full-response endpoint – get the raw `ChatResponse` with metadata (tokens used, finish reason, etc.)
- Streaming endpoint – receive the response token-by-token via Server-Sent Events (SSE)
- Template-based endpoint – use a named `PromptTemplate` to compose structured prompts

## Prerequisites

- Java 21+
- Maven 3.9+
- An [OpenAI API key](https://platform.openai.com/api-keys)

## Configuration

Set your OpenAI API key as an environment variable before running:

```bash
export SPRING_AI_OPENAI_API_KEY=sk-...
```

Alternatively, set `spring.ai.openai.api-key` directly in `src/main/resources/application.properties` (do **not** commit secrets to version control).

## Running the Application

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## API Endpoints

| Method | Endpoint                  | Description                                           |
|--------|---------------------------|-------------------------------------------------------|
| GET    | `/api/chat`               | Simple chat. `?message=<text>` (default: "Tell me a joke") |
| GET    | `/api/chat/full`          | Full `ChatResponse` object as JSON                    |
| GET    | `/api/chat/stream`        | SSE streaming chat                                    |
| GET    | `/api/chat/template`      | Prompt-template chat. `?subject=<topic>`              |

### Examples

```bash
# Simple chat
curl "http://localhost:8080/api/chat?message=What+is+Spring+AI"

# Full response with metadata
curl "http://localhost:8080/api/chat/full?message=Hello"

# Streaming (SSE)
curl -N "http://localhost:8080/api/chat/stream?message=Tell+me+a+story"

# Template-based
curl "http://localhost:8080/api/chat/template?subject=Machine+Learning"
```

## Running Tests

```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   ├── java/com/shantanu/workingwithllm/
│   │   ├── WorkingWithLlmApplication.java   # Spring Boot entry point
│   │   ├── controller/
│   │   │   └── ChatController.java          # REST endpoints
│   │   └── service/
│   │       └── ChatService.java             # Spring AI chat logic
│   └── resources/
│       └── application.properties           # App configuration
└── test/
    └── java/com/shantanu/workingwithllm/
        ├── ChatControllerTest.java           # Controller slice tests
        └── ChatServiceTest.java             # Service unit tests
```

## Dependencies

| Dependency                                  | Purpose                           |
|---------------------------------------------|-----------------------------------|
| `spring-boot-starter-web`                   | REST API                          |
| `spring-ai-openai-spring-boot-starter`      | Spring AI OpenAI integration      |
