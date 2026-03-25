# Vulnerable Java Spring Application

A Spring Boot application containing intentionally vulnerable code patterns for testing application security tools. Each pattern exercises a distinct data-flow complexity, making this a practical benchmark for taint analysis engines.

## Vulnerability Patterns

### Spring Indirect Data Flow

**DI resolution**: DTO field access through DI-resolved service, configuration-sensitive resolver (unsafe vs hardened)

**Cross-endpoint persistence**: taint flow through JPA save/load cycle, inter-procedural getter through DI-resolved `@Service`, column-level sensitivity, field-level sanitization in entity constructor, `@Service` field state across requests, mid-flow sanitizer

**Async coroutines (SSRF)**: user-controlled URL fetched via `URI.toURL().openConnection()` inside a Kotlin `CoroutineScope.launch` on `Dispatchers.IO`, with taint flowing through a data-class DTO and a `CompletableDeferred` bridge

## Tech Stack

- Java 21 + Kotlin 1.9
- Spring Boot 3.3 (Web, Thymeleaf, Data JPA)
- FreeMarker 2.3
- H2 (in-memory)
- Gradle (Kotlin DSL)

## Scanning with OpenTaint

Detect vulnerabilities using [OpenTaint](https://opentaint.org/):

```
opentaint scan .
```

A CI workflow is included at [`.github/workflows/opentaint.yml`](.github/workflows/opentaint.yml) — see [github.com/seqra/opentaint](https://github.com/seqra/opentaint) for setup details.

⚠️ **Warning**: This application contains intentional security vulnerabilities for educational and testing purposes. **Never deploy to production.**

## License

[MIT License](LICENSE)
