# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
./gradlew build              # Build
./gradlew test               # Run all tests
./gradlew bootRun            # Run app (port 8090)
./gradlew flywayMigrate      # Run DB migrations
```

To run a single test class:
```bash
./gradlew test --tests "com.gomcc.merchant_checker_service.service.MerchantServiceTests"
```

Local dev requires PostgreSQL and Redis running. Start them with:
```bash
docker-compose -f compose-dev.yaml up -d --build postgres redis
```

## Architecture

Spring Boot 4.0.1 / Java 21 / Gradle service for fuzzy merchant name lookup. Package: `com.gomcc.merchant_checker_service`.

### Data Flow

Two search paths exist, both exposed under `/public/merchants`:

1. **`/name/{merchantName}`** — `MerchantService.fuzzySearch()` checks Redis first via `MerchantRedisService`, falls back to PostgreSQL `ILIKE` query via `MerchantRepository`.
2. **`/name/fuzzy/{searchWord}`** — Goes directly to `MerchantRedisService.fuzzySearchHashByPattern()` using Redis OM `EntityStream` with wildcard TEXT search. Validates input matches `[0-9a-zA-Z ]+`.

### Dual Storage

- **PostgreSQL** — Primary data store. `Merchant` JPA entity, managed by Flyway migrations (`src/main/resources/db/migration/`). Custom native query with `ILIKE` for fuzzy search.
- **Redis** — `MerchantRedisHash` is a Redis OM `@Document` with `@TextIndexed` on `name` for full-text search. TTL is 3600s (1 hour) on the document level. A separate `RedisCacheManager` bean has a 1-minute TTL for Spring Cache abstraction.

### Cache Warming

`MerchantWarmRedisCacheService` loads all merchants from PostgreSQL into Redis on `ApplicationReadyEvent`. It converts each `Merchant` entity to `MerchantRedisHash` and saves via `MerchantRedisHashRepository`.

### External API

`AskMilesWebClient` calls `https://www.ask-miles.com/api/store` with 2s connect / 6s read timeouts. Used by `MerchantService.getMiles()` (not yet exposed via controller).

### Key Patterns

- **Lombok** throughout: `@Data`, `@Builder`, `@RequiredArgsConstructor` on services/controllers.
- **`@EnableRedisDocumentRepositories`** on the main application class enables Redis OM document scanning.
- **`MerchantRedisHash$`** is a generated metamodel class from Redis OM annotation processor — used for type-safe `EntityStream` queries.
- **`GlobalExceptionHandler`** (`@ControllerAdvice`) handles `ConstraintViolationException`, `ResourceNotFoundException`, and `MethodArgumentTypeMismatchException`.
- Tests use `@MockitoBean` for mocking services and `MockMvc` for controller tests. The `test` task configures a Mockito javaagent via JVM args in `build.gradle`.
