# ADR-03 MCP Transport Protocol

| Status           | accepted       |
|------------------|----------------|
| Decision makers  | Josia Menger   |
| Reviewer         | Justin Wolek   |
| Date of Decision | 19-09-2025     |

## Context

The `McpManagement` microservice provides an MCP server that serves MCP tools. When deployed on our server, the service was not accessible. The root cause was identified as an issue with our proxy, which has problems with the long-lasting HTTP connections used by the Server-Sent Events (SSE) based MCP server.

## Considered Options

### Option 1: Expose the container directly through a port

Expose the Docker container's port directly, bypassing the proxy for MCP server traffic.

### Option 2: Use the new Streamable HTTP or Stateless Transport Protocols

Adopt newer transport protocols for the MCP server as SSE is being deprecated in newer MCP versions.

#### Option 2a: Stateless Transport Protocol

- As suggested by Spring documentation for microservices.
- Does not support updates on tool changes.
- Not officially supported by the MCP Specification (would be a Custom Transport Protocol).
- No tool context support.

#### Option 2b: Streamable HTTP Transport Protocol

- The base implementation is identical to the SSE MCP server, using the same class for representation.
- Spring introduces a new abstraction layer for the transport protocol with the `McpServerTransportProviderBase` interface, implemented by the old `McpServerTransportProvider` and the new `McpStreamableServerTransportProvider`.
- The `McpAsyncServer` now features different constructors to accommodate these changes.
- New Builder classes (e.g., `StreamableServerAsyncSpecification`) are available for easier creation of streamable MCP servers.

## Decision

The team has decided to go with **Option 2b: Streamable HTTP Transport Protocol**.

We require the ability to automatically update the tools list at runtime. The streamable approach provides a solid, future-proof solution that supports session management and runtime tool updates, moving away from deprecated SSE features.

## Consequences

- The implementation will be updated to use the new streamable MCP server from Spring.
- This involves using `McpStreamableServerTransportProvider` and the corresponding builders to construct the MCP server.
- The service will no longer rely on long-lasting HTTP connections that are incompatible with our proxy.

## Pros and Cons of the considered options

### Option 1: Expose the container directly through a port

- **Pros:**
  - Easy and quick workaround.
- **Cons:**
  - Callers of the MCP server would need to know the exact Docker port.
  - Potential for port conflicts if multiple containers expose the same port.

### Option 2: Use the new Streamable HTTP or Stateless Transport Protocols

- **Pros:**
  - Streamable is the new standard in MCP, replacing the deprecated SSE. This is a more future-proof solution.
- **Cons:**
  - Requires more implementation and research effort compared to Option 1.

#### Option 2a: Stateless

- **Pros:**
  - Recommended by Spring documentation for microservices.
- **Cons:**
  - No updates on tool changes.
  - Not officially supported by the MCP Specification.
  - No tool context support.

#### Option 2b: Streamable

- **Pros:**
  - Supports session management and tool updates on runtime.
  - Solid, future-proof solution using the latest MCP standards.
  - The base implementation is very similar to the existing SSE server, which might ease migration.
- **Cons:**
  - Higher initial implementation effort than a simple workaround.
