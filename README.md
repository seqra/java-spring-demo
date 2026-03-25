# Vulnerable Java Spring Application

A Spring Boot application containing intentionally vulnerable code patterns for testing application security tools. Each pattern exercises a distinct data-flow complexity, making this a practical benchmark for taint analysis engines.

## Vulnerability Patterns

Intentionally vulnerable patterns, grouped by category:

### XSS Complexity

1. Direct user input return
2. Local variable assignment
3. Inter-procedural flow
4. Constructor chains and field sensitivity
5. Builder pattern and virtual method calls

## Scanning with OpenTaint

Detect vulnerabilities using [OpenTaint](https://opentaint.org/):

```
opentaint scan .
```

A CI workflow is included at [`.github/workflows/opentaint.yml`](.github/workflows/opentaint.yml) — see [github.com/seqra/opentaint](https://github.com/seqra/opentaint) for setup details.

⚠️ **Warning**: This application contains intentional security vulnerabilities for educational and testing purposes. **Never deploy to production.**

## License

[MIT License](LICENSE)
