# ADR-01 Modularization Strategy

| Status           | accepted       |
|------------------|----------------|
| Decision makers  | GroKoSoMo Team |
| Date of Decision | 24-07-2025     |

## Context

One of the main goals of the GroKoSoMo project is to create a modular and maintainable codebase that can evolve over time.
The codebase should be spread across multiple microservices, each responsible for a specific domain or functionality.
This modularization will help in managing complexity, improving maintainability, and enabling independent development and deployment

## Considered Options

### Option 1: Domain-Driven Design (DDD)

Split the application into modules based on business domains. 
Each module encapsulates its own domain logic, data access, and user interface components.

- MCP tool management
- User management
- Open API 2 tool conversion

### Option 2: Technology-Driven/ Layered Design

Split the application into modules based on technology layers (e.g., presentation, business logic, data access).

- UI 
- Business Logic
- Data Access

## Decision

After evaluating and discussing the options, the team has decided to go with **Option 1: Domain-Driven Design (DDD)**. 
The ui microservice is the only exception, as it is not a domain but rather a technology layer that serves the user interface for all domains.

## Consequences

All further design decisions and implementations will follow the Domain-Driven Design approach,
ensuring that each module is focused on a specific business domain.

## Pros and Cons of the considered options

### Option 1: Domain-Driven Design (DDD)

- **Pros:**
  - Aligns with business domains, making it easier to understand and maintain.
  - Each module can evolve independently, allowing for faster development cycles.
  - Encourages separation of concerns, leading to cleaner code.

- **Cons:**
  - Requires careful design to avoid tight coupling between modules.
  - May lead to duplication of code if not managed properly.
  - Initial setup and design can be more complex compared to a technology-driven approach.

### Option 2: Technology-Driven/ Layered Design

- **Pros:**
  - Simpler to implement initially, as it follows a familiar layered architecture.
  - Easier to manage shared components across different layers.
  - Can be more straightforward for teams with experience in traditional layered architectures.

- **Cons:**
  - Can lead to tight coupling between layers, making it harder to change one layer without affecting
  - A single request may need to traverse multiple layers, leading to a lot of requests which can impact performance.
  - Less aligned with business domains, making it harder to understand the overall system from a business perspective