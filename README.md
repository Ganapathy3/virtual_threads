# Day 2 — Java 21 Virtual Threads (Project Loom) 🧵

**Part of my Java Trends 2026 series** — one hands-on topic per day.

## What is this?

5 runnable demos showing everything you need to know about Java 21 Virtual Threads — from creation to performance comparison to Spring Boot integration.

**No extra dependencies.** Virtual Threads are built into Java 21 core.

## The Big Idea

| | Platform Thread | Virtual Thread |
|---|---|---|
| Memory | ~1 MB each | ~few KB each |
| Max count | Thousands | Millions |
| Blocked on I/O | Wastes OS thread | Parks, frees carrier |
| Best for | CPU-heavy work | I/O-bound work (DB, HTTP) |
| Created since | Java 1 | Java 21 (GA) |

## Demos

| Demo | What it shows |
|------|--------------|
| Demo 1 | 3 ways to create virtual threads |
| Demo 2 | Performance: 1000 tasks — platform vs virtual |
| Demo 3 | Parallel microservice calls — total time = slowest call |
| Demo 4 | Common mistakes: avoid `synchronized`, use `ReentrantLock` |
| Demo 5 | Enable in Spring Boot with one line of config |

## Quick Start

### Requirements
- Java 21+
- Maven 3.8+

```bash
java -version  # must be 21+
```

### Run all demos
```bash
mvn compile exec:java -Dexec.mainClass="com.demo.vthreads.Main"
```

### Expected output (Demo 2 example)
```
Platform threads (pool=200): 1000 tasks in ~550 ms
Virtual threads (unlimited): 1000 tasks in ~110 ms
Virtual threads were ~5x faster!
```

## Key Takeaways

- Virtual threads use the same `Thread` API — no new learning curve
- `Executors.newVirtualThreadPerTaskExecutor()` is your best friend
- Replace `synchronized` with `ReentrantLock` to avoid pinning
- Spring Boot 3.2+: add `spring.threads.virtual.enabled=true` — done!
- Best for I/O-bound work (DB queries, HTTP calls, file reads)

## Spring Boot Integration (from Day 1)

Add to `application.properties` in your Day 1 Spring AI project:

```properties
spring.threads.virtual.enabled=true
```

Every HTTP request to your `/ask` endpoint now runs on a virtual thread!

---
> Part of my **Java Trends 2026** daily commit series.
