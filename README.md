# Generic MCP Server

[TOC]

# MCP (Model Context Protocol)

## Overview
server client architecture:
- Hosts: LLM applications (Claude, IDEs, etc.), initiate connections
- Clients: maintain 1:1 connection with a server; inside the host application
- Servers: provide context, tools, prompts to clients

```
------- Host -------                         -- Server Process --
|  --------------  |                         |  --------------  |
|  | MCP Client | <----- Transport Layer -----> | MCP Server |  |
|  --------------  |                         |  --------------  |
|                  |                         --------------------
|                  |                         -- Server Process --
|  --------------  |                         |  --------------  |
|  | MCP Client | <----- Transport Layer -----> | MCP Server |  |
|  --------------  |                         |  --------------  |
--------------------                         --------------------
```

### Transport Layer
- JSON RPC 2.0
-

- Stdio transport:
    - Uses stdin/stdout for communication
    - for local processes

- Streamable HTTP transport:
    - uses HTTP with optional server-sent events (SSE) for streaming
    - HTTP POST for client-to-server messages


## Features
- Tools
- Resources
- Prompts

# Name ideas

- REST2MCP
- MCPort
- RestifyMCP
- BridgeMCP
- NeuroBridge



# Links and References

- Open AI Specification (v.3.1.1): [OpenAI Spec](https://spec.openapis.org/oas/latest.html)
- MCP Specification: [MCP Spec](https://modelcontextprotocol.io/specification)
- Tool Calling, Spring AI Documentation: [Tool Calling](https://docs.spring.io/spring-ai/reference/api/tools.html)
- MCP Server Boot Starter:
  [MCP Server](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html)

  
