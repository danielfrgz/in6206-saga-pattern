# Saga Pattern — Orchestration Demo

A minimal implementation of the Saga pattern for distributed transactions,
built with Spring Boot. Coursework for IN6206 (Group Assignment 1),
Nanyang Technological University Master of Science in Information Systems (MSIS).

## Overview

Four services cooperate to fulfil an order. Each owns its own database, so no
single ACID transaction spans the workflow. Consistency is maintained instead
by a saga: a sequence of local transactions, each with a compensating action
that semantically undoes it if a later step fails.

Coordination is by **orchestration** — a central orchestrator drives each step
and triggers compensation in reverse order on failure.

## Architecture

| Service | Port | Responsibility |
| `orchestrator-service` | 8080 | Drives the saga, handles compensation |
| `order-service` | 8081 | Creates and cancels orders |
| `payment-service` | 8082 | Charges and refunds payments |
| `inventory-service` | 8083 | Reserves and releases stock |

Happy path: `createOrder → chargePayment → reserveStock → confirmOrder`

Failure path: if stock reservation fails, the orchestrator issues a refund and
cancels the order. The payment is not rolled back — it is *compensated* by a
refund, which is a business operation, not a database one.

## Tech stack

Java 21 · Spring Boot · Spring Data JPA · H2 (in-memory, one per service) ·
Maven · Postman

## Running

Start each service in its own terminal:

```bash
cd order-service && ./mvnw spring-boot:run
```

Repeat for `payment-service`, `inventory-service`, `orchestrator-service`.

Then import `postman/saga-demo.postman_collection.json` and run:

1. **Happy path** — order within available stock
2. **Failure path** — order exceeding available stock, triggering compensation
3. **Order status** — poll to observe the transition through `PENDING`

Each service exposes its H2 console at `/h2-console` for inspecting state
before and after a run.

## Repository layout

```
order-service/          Order aggregate and cancellation endpoint
payment-service/        Charge and refund endpoints
inventory-service/      Stock reservation and release
orchestrator-service/   Saga state machine and compensation logic
postman/                Exported request collection
docs/                   Sequence diagrams
```

## Group

| Name | Matric |
| Daniel Frutos Rodriguez | G2604692C |
| Devadathan Menon | G2609217J |
| Jatin Sharma | G2604683L |
| Pimpale Padmakar Rajendra | G2605578K |
| Loe Jun Xiang | G2605613F |