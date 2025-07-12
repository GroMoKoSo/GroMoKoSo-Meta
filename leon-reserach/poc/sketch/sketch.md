# 🛠️ Dynamic Tool Architecture for Auto-Registering Tools from API Specifications

## 🧹 Goal

This architecture enables the dynamic creation and registration of tool callbacks (`ToolCallback`) at runtime based on a list of API specifications. It’s designed to support systems such as AI agents, allowing them to interface with external services without requiring manual implementation for each tool.

---

## 🧱 Core Components

### 1. `ApiToolSpec`

A simple data record or DTO that describes an API-based tool. It contains metadata and technical details such as:

* Tool name and description
* HTTP method (`GET`, `POST`, etc.)
* Target endpoint (e.g., `/api/users/search`)

### 2. `ToolCallback` (Interface)

An interface that defines the contract for an executable tool. Each tool must implement this to be recognized by the system. Key methods include:

* `getName()`
* `getDescription()`
* `call(ToolInput input)`

### 3. `GenericToolCallback` (Implementation)

A generic implementation of `ToolCallback` that uses the `ApiToolSpec` to make dynamic HTTP requests. It acts as a wrapper that delegates input/output handling to a generic HTTP client, making it possible to generate tools from simple specs.

### 4. `ToolFactory` (Interface)

Defines a factory that takes a list of `ApiToolSpec` and produces a list of `ToolCallback` instances.

### 5. `DefaultToolFactory` (Implementation)

Concrete implementation of the `ToolFactory` interface. It converts each `ApiToolSpec` into a corresponding `GenericToolCallback`.

### 6. `ToolCallbackProvider` (Interface)

An extension point used by Spring Boot to automatically discover and register tool callbacks. The factory-produced tools are exposed via this provider.

### 7. `ToolProviderConfig` (Spring Configuration)

A Spring configuration class that:

* Loads the list of `ApiToolSpec` (e.g., from a config file, database, or service discovery).
* Uses the `ToolFactory` to create `ToolCallback` instances.
* Registers them via a `ToolCallbackProvider` bean.

---

## 🔄 System Workflow

1. A list of API tool specs is loaded from a source (file, DB, service registry, etc.).
2. `DefaultToolFactory` generates a `GenericToolCallback` for each API spec.
3. These tools are exposed via a `ToolCallbackProvider` bean.
4. Spring Boot auto-configuration discovers and registers the tools — without any manual wiring.

---

## ⚙️ Extension Opportunities

* Additional `ToolCallback` implementations (e.g., for GraphQL or gRPC).
* Enriched `ApiToolSpec` with authentication, validation rules, or schema definitions.
* Pluggable `ToolFactory` variants (e.g., `OpenApiToolFactory`).

---

## ✅ Advantages

* **Dynamic**: Tools can be introduced without changing the code.
* **Maintainable**: Adding tools is a matter of configuration, not implementation.
* **Scalable**: Easily supports a large number of tools.
* **Flexible**: Decoupled design makes extension or replacement trivial.
