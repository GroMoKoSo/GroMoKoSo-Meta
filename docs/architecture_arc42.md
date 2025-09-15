
# GroßsprachmodellModellKontextProtokollServerOrchestrierung 

(Abk.: GroMoKoSo)


[[_TOC_]]

# 0. Terminology and Conformance Language
Normative text describes one or both of the following kinds of elements:

Vital elements of the specification
Elements that contain the conformance language keywords as defined by IETF RFC 2119 "Key words for use in RFCs to Indicate Requirement Levels"
Informative text is potentially helpful to the user, but dispensable. 
Informative text can be changed, added, or deleted editorially without negatively affecting the implementation of the specification. 
Informative text does not contain conformance keywords.

All text in this document is, by default, normative.

The keywords "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this document are to be interpreted as described in IETF RFC 2119 "Key words for use in RFCs to Indicate Requirement Levels".

According to: https://datatracker.ietf.org/doc/rfc2119/

# 1. Introduction and Goals

Describes the relevant requirements and the driving forces that software
architects and development team must consider. These include

-   underlying business goals,
-   essential features,
-   essential functional requirements,
-   quality goals for the architecture and
-   relevant stakeholders and their expectations

## 1.1 Goals
Our goal is to develop a platform which allows users to integrate existing RESTful APIs
and automatically create [MCP](#12-model-context-protocol-mcp) Tools which empower the capabilities of Large Language Models (LLMs).
Users should be able to define API specifications in a WYSIWYG interface or upload OpenAPI specifications.


## 1.2 Model Context Protocol (MCP)

Full MCP specification: [Model Context Protocol](https://modelcontextprotocol.io/specification/2025-06-18)

The Model Context Protocol (MCP) is an open, standardized interface that allows
Large Language Models (LLMs) to interact seamlessly with external tools, APIs,
and data sources. It provides a consistent architecture to enhance AI model
functionality beyond their training data, enabling smarter, scalable, and more
responsive AI systems.

### 1.2.1 Core Components

server client architecture:
- Hosts: Contains one or more instance of LLMs which use one or more MCP clients which handle the communication with the MCP Server.
- Clients: maintain 1:1 connection with a server; inside the host application
- Servers: provide context, tools, prompts to clients

```
------- Host -------                         -- Server Process --
|  --------------  |                         |  --------------  |
|  | MCP Client | <----- Transport Layer -----> | MCP Server |  |
|  --------------  |                         |  --------------  |
|                  |                         --------------------
|                  |
|                  |                         -- Server Process --
|  --------------  |                         |  --------------  |
|  | MCP Client | <----- Transport Layer -----> | MCP Server |  |
|  --------------  |                         |  --------------  |
--------------------                         --------------------
```

## 1.3 Stakeholder

| Stakeholder    | Role          | Goal                                                                                                          | Expectations             |
|----------------|---------------|---------------------------------------------------------------------------------------------------------------|--------------------------|
| M. Münker      | Product Owner | Architecture and software that enables the dynamic and reusable provision of services via MCP for AI agents.  | Interesting architecture |
| Dev Team       | SW Devs       | Granular architecture, easy to maintain and expandable                                                        |                          |
| General Public | User          | Easy to use, intuitive interface                                                                              |                          |

## 1.4. Requirements Overview

![Use Case diagram](/docs/diagrams/use_case.svg)

TODO: Add user and System Requirements

| Short | Meaning  | Description                                                                                           |
|-------|----------|-------------------------------------------------------------------------------------------------------|
| `M`   | Must     | Must be fulfilled, otherwise the architecture is not acceptable                                       |
| `O`   | Optional | Optional, but desirable, requirements that can be fulfilled later                                     |
| `Q`   | Quality  | Quality requirements that are not directly related to the architecture, but to the system as a whole  |

`<type>`-`<id>` e.g. `M-1`, `O-1`, `Q-1`

## 1.4.1 Non-functional Requirements / Quality Goals

Quality goals as defined by [ISO 25010](https://www.iso.org/obp/ui/#iso:std:iso-iec:25010:ed-2:v1:en) 

| Quality goal           | Description                                                                                                                                                                                                                                |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Functional suitability | Capability of a product to provide functions that meet stated and implied needs of intended users when it is used under specified conditions                                                                                               |
| Performance efficiency | Capability of a product to perform its functions within specified time and throughput parameters and be efficient in the use of resources under specified conditions                                                                       |  
| Compatibility          | Capability of a product to exchange information with other products, and/or to perform its required functions while sharing the same common environment and resources                                                                      |
| Interaction capability | Capability of a product to be interacted with by specified users to exchange information between a user and a system via the user interface to complete the intended task                                                                  |
| Reliability            | Capability of a product to perform specified functions under specified conditions for a specified period of time without interruptions and failures                                                                                        | 
| Security               | Capability of a product to protect information and data so that persons or other products have the degree of data access appropriate to their types and levels of authorization, and to defend against attack patterns by malicious actors |
| Maintainability        | Capability of a product to be modified by the intended maintainers with effectiveness and efficiency                                                                                                                                       |
| Flexibility            | Capability of a product to be adapted to changes in its requirements, contexts of use, or system environment                                                                                                                               |
| Safety                 | Capability of a product under defined conditions to avoid a state in which human life, health, property, or the environment is endangered                                                                                                  |


Priorities

| Prio | Description         | Explanation                                                          |
|------|---------------------|----------------------------------------------------------------------|
| 1    | Extremely important | Compromises only when higher priority features are strengthened.     |
| 2    | Important           | Compromises are possible when core requirements are not compromised. |
| 3    | Significant         | Compromises are possible when core requirements are not compromised. |
| 4    | Insignificant       | This feature should only be taken into account to a limited extent.  |

| ID  | Prio | Quality Goal           | Description                                   |
|-----|------|------------------------|-----------------------------------------------|
| Q-1 |      | Interaction capability | Project requires complete documentation       |
| Q-2 |      | Interaction capability | Code should be modular and reusable           |
| Q-3 |      | Usability              | Easy to use, intuitive interface              |
| Q-4 |      | Security               | Authentification against a OAuth Server       |


## 1.4.2 Functional Requirements
| ID  | Requirement                                                   | Description                                                                                                 |
|-----|---------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| M-1 | Parse OpenApi Specs to internal API-representation            | The Software MUST be able to convert any OpenApi specification to a more abstract, internal representation. |
| M-2 | Create internal API-representation by using a WYSIWYG-editor  | The User MUST be able to add a new tool to the MCP Server by adding it's API within the UI.                 |
| M-3 | Create MCP-Tools from internal API-representation             | The Software MUST be able to create MCP-Tools by using Spring Boot's MCP-Library                            |
| M-4 | Serve MCP-Tools to MCP-Client                                 | Serving MCP-Tools by complying to MCP                                                                       |
| M-5 | Authentification against THM oauth2 Server                    | The User of the Software MUST authenticate against the THM oauth2 Server using OpenID Connect.              |
| O-1 | Support RAML Specs                                            | In addition to Open-Api specs, API specs as RAML should also be supported                                   |
| O-2 | Support Authentication for MCP Tools                          | The software MAY provide sufficient OAuth2 based Authentication mechanisms for MCP-Tools.                   |
| O-3 | REST-API secrets should be stored securely                    | The Software MAY store secrets (e.g. API-Keys) securely, e.g. in a vault or encrypted database.             |

# 2. Architecture Constraints
Any requirement that constraints software architects in their freedom of
design and implementation decisions or decision about the development
process. These constraints sometimes go beyond individual systems and
are valid for whole organizations and companies.


## 2.1 Organizational constraints

| ID | Constraint             | Description                                                                                                                                                                                               |
|----|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | Team members are fixed | **Team**: <ul><li>Heimann, Jannik</li><li>Lange-Hermstädt, Benjamin Michael</li><li>Menger, Josia</li><li>Schäfer, Leon</li><li>Wolek, Justin</li></ul>No new team members are expected to join the team. |
| 2  | Presentation           | Presentation on 09.09.2025                                                                                                                                                                                | 
| 3  | Deadline               | The projects MUST be finished by 02.10.2025                                                                                                                                                               |
| 4  | Planning               | Planning MUST be done inside the GitLab issue board.                                                                                                                                                      |
| 5  | Documentation          | Documentation is done inside the GitLab wiki. According to the [Arc42 Template](https://arc42.de/), All diagrams SHOULD be available as images inside the documentation                                   |                     


## 2.2 Technical constraints
| ID | Constraint   | Description                                                         |
|----|--------------|---------------------------------------------------------------------|
| 1  | Technologies | MUST: Java, Spring Boot, ThymeLeaf                                  |  
| 2  | Architecture | RESTful API MUST be used.                                           |                                 
| 3  | Deployment   | Deployment MUST be done within the university server infrastruture. |


# 2.3 Conventions
Programming Conventions are documented on the wiki page in Gitlab. And can be found [here](https://git.thm.de/softwarearchitektur-wz-ss24/studentswa2025/enton/gromokoso/-/wikis/home/conventions).


# 3. Context and Scope

The context boundary represents the `GroMoKoSo` system in relation to its external interfaces, users, and neighboring systems.
The goal of this chapter is to make the system's communication relationships with its environment transparent.

## 3.1 Business Context
![Business context diagram](/docs/diagrams/level_0_context/business_context.svg)

| Element       | Description                                                                                          |
|---------------|------------------------------------------------------------------------------------------------------|
| System Member | Person who uses an MCP-Host which uses the provided tools by the GroMoKoSo system.                   |
| Group Member  | Person who is part of a corresponding group and which MCP Host has access to the tools of the group. |
| Group Editor  | Person who manages tools and configures new tools inside a group.                                    |
| Group Admin   | Person who manages user, and tools within a corresponding group.                                     |
| System Admin  | Person who has access to all users, groups and tools and can manage all of them.                     |
| MCP Host      | Software system that allows a LLM Tool to use a MCP server to fulfill complex requests.              |
| REST APIs     | External Interfaces from which the System can create a tool which can be used by the MCP Host.       |

## 3.2 Technical Context
![Technical context diagram](/docs/diagrams/level_0_context/technical_context.svg)

| Element                  | Interfaces | Description                                                                                                |
|--------------------------|------------|------------------------------------------------------------------------------------------------------------|
| User                     | html, http | Person who uses an MCP-Host to take advantage of the MCP-Server to fulfill his requests.                   |
| UI                       | http       | User Interface. See [4.2.1](#421-UI)                                                                       |
| UserManagement           | http       | See [4.2.2](#422-UserManagement)                                                                           |
| ApiManagement            | http       | See [4.2.3](#423-ApiManagement)                                                                            |
| McpManagement            | http       | See [4.2.4](#424-McpManagement)                                                                            |
| Spec2Tool                | http       | See [4.2.5](#425-Spec2Tool)                                                                                |
| MCP Client               | http, sse  | Client used by an LLM to communicate with the MCP Server. A client has a 1 to 1 relationship with a server |
| LLM                      |            | Large Language Model                                                                                       |
| REST APIs                | http       | External Interfaces from which the System can create a tool which can be used by the MCP Host.             |
| OpenID Identity Provider | http       | Server against which the user and the request MUST be identified.                                          |

# 4. Solution Strategy
## 4.1 Technology Decisions

Technologies which will be used are **Java** and **Spring Boot** as requested by the stakeholder M. Münker.
The project goal also requires the implementation of MCP-Tools.

The system will be separated into multiple microservices which will be deployed as Docker containers.
Each service will be responsible for a specific domain except the UI service.
The decision to use microservices instead of a monolithic architecture was taken with respect to 
expandability, maintainability, reusability and interchangeability. 
The decision to use microservices was also taken with respect to the stakeholders requirements to provide a 
complex architecture with the attributes mentioned above.

These services include:
- McpManagement
- UserManagement
- ApiManagement
- Spec2Tool
- UI

Each microservice including their responsibility and purpose is described in [4.2](#42-subservices)

## 4.2 Subservices

### 4.2.1 UI
UI provides a graphical user interface for the user to interact with the system itself. Each use-case which has been
defined is covered by the UI. UI uses REST endpoints which are provided by each microservice to perform actions.

### 4.2.2 UserManagement
User management is responsible for managing access-rights for MCP tools which are split between users and groups.
When actions like the execution of MCP tools or modifying APIs are made, User management provides a method to check
whether these operations should be authorized.

### 4.2.3 ApiManagement
Api management is the entrypoint for creating, modifying and deleting APIs which should be used as MCP Tools.
Api management uses user management to check if operations should be allowed, Spec2Tool to convert OpenAPI specs to
MCP tools and mcp management to finally create these MCP tools.

### 4.2.4 McpManagement
MCP management will provide Servers dynamically depending on user requests. Each user will have their own MCP Server
to ensure that the corresponding tools of the user will be available. Therefore the usage of the servers will be
monitored and additional servers will be started or shut down if needed.

### 4.2.5 Spec2Tool
Spec2Tool is responsible for converting OpenAPI specs to an internal representation of MCP tools. These representations
are used by mcp management to create MCP tools.

## Organizational Decisions

The necessary work which needs to be done to reach the predefined project-goals should be done entirely within the team.
To achieve the quality goals, a version control system (GitLab) shall be used to manage the source code and documentation.
This also includes the commitment to review any code-changes which should be done.

# 5. Building Block View

All diagrams in this chapter use the following legend.

![Building block legend](/docs/diagrams/building_block_legend.svg)

## 5.1 Level 1: Subsystems/ Mirco services

![GroMoKoSo Subsystems](/docs/diagrams/level_1_subsystem/gromokoso_subsystems.svg)

| Element               | Dependencies                             | Description                                                                                                                                                      |
|-----------------------|------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| User (external)       | UI                                       | A person using the GroMoKoSo system. This can either be a administrator, tool manager or regular user.                                                           |
| Mcp Client (external) | McpManagement                            | The GroMoKoSo system supports every client that complies with the mcp specification (Claude, Github Copilot, ...).                                               |
| UI                    | UserManagement, ApiManagement            | Provides an interactive easy to use graphical user interface as a webapp.                                                                                        |
| UserManagement        | -                                        | Manage user, groups and permissions. A user can have tools and be part of multiple groups. A group can also have tools.                                          |
| ApiManagement         | UserManagement, Spec2Tool, McpManagement | Manage apis, specifications and tokens. This service is responsible for every external api call on behalf of the system.                                         |
| McpManagement         | UserManagement, ApiManagement            | Manage and host mcp servers with tools. Routes mcp requests from users to its personal mcp servers and call apis using ApiManagement to fulfill client requests. |
| Spec2Tool             | -                                        | Convert common api specifications formats to GroMoKoSos internal api/tool representation.                                                                        |

template:
-   Purpose/Responsibility
-   Interface(s), when they are not extracted as separate paragraphs.
    This interfaces may include qualities and performance
    characteristics.
-   (Optional) Quality-/Performance characteristics of the black box,
    e.g.availability, run time behavior, ....
-   (Optional) directory/file location
-   (Optional) Fulfilled requirements (if you need traceability to
    requirements).
-   (Optional) Open issues/problems/risks

## 5.2 Level 2: Container View

### 5.2.1 UI

![UI Containers](/docs/diagrams/level_2_container/ui_subsystem_container_view.svg)

The UI container delivers the web application and acts as a frontend for GroMoKoSo. 
It renders all user flows (sign-in, API/spec management, tool creation, group membership, tool usage dashboards) and orchestrates calls to backend services to execute actions initiated in the browser.
The UI is implemented using React, TypeScript, and Vite. It is served as a single-page application (SPA) that communicates with backend microservices exclusively via RESTful HTTP endpoints. The UI itself does not expose a public REST API, it only provides browser-facing routes and assets.

Consumed Interfaces:
- **UserManagement**: perform CRUD operations on users, groups, and roles, including role assignment and group membership management.
- **ApiManagement**: upload/retrieve API specifications, trigger tool registration/lifecycle operations, and query API/token metadata.

Authentication is performed via OpenID Connect (THM OAuth2). The UI MUST enforce role-aware navigation and ensure backend authorization checks before executing protected operations.


[Link to Mock-Ups: excalidraw board](https://excalidraw.com/#room=e2d64da42ffe07c87d8e,l5fMXfX1RgUFZkcbNKHvIQ)

### 5.2.2 UserManagement
![UserManagement Containers](/docs/diagrams/level_2_container/user_management_subsystem_container_view.svg)

The purpose of this Microservice is to provide and save all relevant data regarding the users and groups which will be using this Application.
This data consists of:
- User data:
    - Unique username
    - First- and Lastname
    - E-Mail
    - System Role which can be one of the following:
      - System member
      - System admin
    - API Id's which the user has access to and whether they are currently active.
- Group data:
  - Unique name of the group.
  - Users which can have the following roles within the group:
    - Group member
    - Group editor
    - Group admin
  - API Id's which the users of the group have access to.
  - Group Type which can be one of the following:
    - Private
    - Public

In the following table is an overview which further describes the mentioned roles or types above:  

| Role / Type   | Description                                            | Rights                                                                                                                                                                                                |
|---------------|--------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| System member | Standard role in the System.                           | MUST be able to create and use personal tools.<br/> MUST be able to join public groups and leave any joined groups.<br/>MUST be able to use tools from groups they belong to.                         |
| System Admin  | System wide admin role.                                | MUST be able to create users, assign roles, and create/delete groups.<br/>MUST be able to manage all groups, including assigning group roles.                                                         |
| Group member  | Standard role within groups.                           | MUST be able to use tools of the group.<br/>MUST be able to leave the group.                                                                                                                          |
| Group editor  | Allowed to edit tool settings within the group.        | MUST be able to create and edit tools within the group.                                                                                                                                               |
| Group admin   | Allowed to manage tools and members within the group.  | MUST be able to create and edit tools within the group.<br/>MUST be able to manage group members (add/remove/edit roles).<br/>MUST be able to edit the group profile (name, description, visibility). |
| Private group | Group is only visible for corresponding group members. | -                                                                                                                                                                                                     |
| Public group  | Group is visible to all System members.                | -                                                                                                                                                                                                     |


The interfaces to request or update the data of the user management are defined as a REST API in the [User management REST API documentation](/docs/interfaces/user_management.yaml)

### 5.2.3 ApiManagement

![ApiManagement Containers](/docs/diagrams/level_2_container/api_management_subsystem_container_view.svg)

The ApiManagement Container is responsible for handling all external API interactions and the management of apis, specifications and tokens.
Its central component, the ApiProxy container, provides REST endpoints that allow the UI to retrieve API specifications
and the McpManagement subsystem to invoke external API calls.
In addition, ApiProxy manages tool registration and delegates specification conversion tasks to the Spec2Tool subsystem.
To ensure secure communication, ApiProxy stores and retrieves authentication tokens from the ApiDB container using SQL queries.
This design cleanly separates responsibilities:

- ApiProxy acts as the gateway for specification handling and API invocation
- ApiDb ensures reliable credential storage.

Together, these containers form a cohesive unit that abstracts the complexity of external APIs and provides a uniform interface to the rest of the system.

The interfaces which the API management offers are defined as a REST API in the [API management REST API documentation](/docs/interfaces/api_management.yaml)

### 5.2.4 McpManagement

![McpManagement Containers](/docs/diagrams/level_2_container/mcp_management_subsystem_container_view.svg)

MCP management subsystem is responsible for providing MCP tools to each user.
When using an MCP tool the request is forwarded to the API management subsystem which handles calling the related external APIs.

Tools themselves are registered by the API management subsystem using a REST API the mcp server provides.
To get the mapping between users and their tools, the UserManagement subsystem is used.

Each active user receives their own MCP server which only contains MCP tools they should have access to.
After a certain amount of time mcp-servers get shutdown to save on ressources.

Tools are saved inside the ToolDb database container. Each tool instance belongs to a toolset.
Each toolset may have zero or more tools associated.

The interfaces which MCP management offers are defined as a REST API in the [MCP management REST API documentation](/docs/interfaces/mcp_management.yaml)

### 5.2.5 Spec2Tool

![Spec2Tool Containers](/docs/diagrams/level_2_container/spec_2_tool_subsystem_container_view.svg)

Spec2Tool is responsible for converting OpenAPI specifications to an internal representation of MCP tools.
To achieve this, Spec2Tool offers a RESTful endpoint to provide specifications as a string which returns the
corresponding tool representation as a JSON structure.

The interfaces which Spec2Tool offers are defined as a REST API in the [Spec2Tool REST API documentation](/docs/interfaces/spec2tool.yaml)

## 5.3 Level 3: Component/ Class View

### 5.3.1 UI

The user interface implements a layer architecture as described in [8.1](#81-multi-layered-architecture)
to cleanly separate different concerns.

![User management component view](/docs/diagrams/level_3_component/user_interface_container_component_view_excalidraw.svg)

| Element     | Description                                         |
|-------------|-----------------------------------------------------|
| Router      | React component that handles routing                |
| Page        | React component that can ge accessed throug a route |


The corresponding class-diagram can be found [here](/docs/diagrams/level_4_class/user_interface_container_component_view_puml.svg).

### 5.3.2 UserManagement
The UserManagement implements a layer architecture as described in [8.1](#81-multi-layered-architecture)
to cleanly separate different concerns.

![User management component view](/docs/diagrams/level_3_component/user_management_container_component_view.svg)

| Element     | Description                                     |
|-------------|-------------------------------------------------|
| Controller  | (REST) Endpoints for external systems           |
| Client      | Calling (REST) Endpoints of other Microservices |
| Service     | Business Logic used by endpoint functions       |
| Repository  | Handles data persistence                        |
| Entity      | Entities and value-objects                      |

The corresponding class-diagram can be found [here](/docs/diagrams/level_4_class/user_management_class_diagram.drawio).
The corresponding entity-relationship-diagram can be found [here](/docs/diagrams/level_4_class/user_management_er_diagramm.drawio).

### 5.3.3 ApiManagement
The ApiManagement implements a layer architecture as described in [8.1](#81-multi-layered-architecture)
to cleanly separate different concerns.

![API Management component view](/docs/diagrams/level_3_component/api_proxy_container_component_view.svg)

| Element    | Description                                                                                                          |
|------------|----------------------------------------------------------------------------------------------------------------------|
| Controller | (REST) Endpoints for external systems                                                                                |
| Service    | Business Logic used by endpoint functions                                                                            |
| Repository | Handles data persistence                                                                                             |
| Entity     | Entities and value-objects                                                                                           |
| Clients    | Cross-cutting concern: Communication with other subsystems (Spec2Tool, McpManagement, UserManagement, external APIs) |

The corresponding class-diagram can be found [here](/docs/diagrams/level_4_class/api-management-class-diagram.drawio).
The corresponding entity-relationship-diagram can be found [here](/docs/diagrams/level_4_class/api-management-er-diagram.svg).

### 5.3.4 McpManagement
The McpManagement implements a layer architecture as described in [8.1](#81-multi-layered-architecture)
to cleanly separate the different concerns.

![Mcp Server Components](/docs/diagrams/level_3_component/mcp_server_container_component_view.svg)

| Element        | Description                                                                                                                |
|----------------|----------------------------------------------------------------------------------------------------------------------------|
| Mcp Client     | Mcp client that confirms to the [mcp specification](https://modelcontextprotocol.io/specification/2025-06-18/client/roots) |
| ApiManagement  | see chapter [5.2.3](#523-apimanagement)                                                                                    |
| UserManagement | see chapter [5.2.2](#522-usermanagement)                                                                                   |
| ToolDb         | SQL Database storing tool definitions                                                                                      |
| Controller     | (REST) Endpoints for external systems                                                                                      |
| Service        | Business logic/ Mcp servers                                                                                                |
| Entity         | Entities (ORM), value objects                                                                                              |
| Repository     | Encapsulate persistence                                                                                                    |
| Security       | Cross-cutting concern: OAuth2 authorization                                                                                |
| Configuration  | Cross-cutting concern: Spring configuration classes                                                                        |
| Client         | Cross-cutting concern: Communication with other subsystems (ApiManagement, UserManagement)                                 |

The class diagram can be found [here](/docs/diagrams/level_4_class/mcp_management_class_diagram.drawio)

### 5.3.5 Spec2Tool
TODO: Add everything 

# 6. Runtime View {#section-runtime-view}

TODO: Add Sequence Diagrams based on Use cases (Also link use cases from beginning)


## Add new MCP Tool
< Which use case >
< Diagram >
< Add description >

## List existing MCP Tools
< Which use case >
< Diagram >
< Add description >

## Use MCP Tool

**Scenario**: A user/ LLM wants to invoke a mcp tool using an MCP client.

![Sequenz diagram](/docs/diagrams/runtime/invoke_mcp_tool.png)

1. The LLM invokes a mcp tool
2. The mcp client sends a post request to the mcp server endpoint containing the parameter for the tool.
3. The McpManagement uses the provided parameter to construct a REST request and call the external api. The call is done through the api proxy.
4. The api proxy adds authentication to the api request and forwards the request to the corresponding external server.
5. The external server returns a response.
6. The api proxy wraps the response in a container to provide a standardized response and proper error handling.
7. response forwarding
8. response forwarding

# 7. Deployment View {#section-deployment-view}

::: formalpara-title
**Content**
:::

The deployment view describes:

1.  technical infrastructure used to execute your system, with
    infrastructure elements like geographical locations, environments,
    computers, processors, channels and net topologies as well as other
    infrastructure elements and

2.  mapping of (software) building blocks to that infrastructure
    elements.

Often systems are executed in different environments, e.g. development
environment, test environment, production environment. In such cases you
should document all relevant environments.

Especially document a deployment view if your software is executed as
distributed system with more than one computer, processor, server or
container or when you design and construct your own hardware processors
and chips.

From a software perspective it is sufficient to capture only those
elements of an infrastructure that are needed to show a deployment of
your building blocks. Hardware architects can go beyond that and
describe an infrastructure to any level of detail they need to capture.

::: formalpara-title
**Motivation**
:::

Software does not run without hardware. This underlying infrastructure
can and will influence a system and/or some cross-cutting concepts.
Therefore, there is a need to know the infrastructure.

Maybe a highest level deployment diagram is already contained in section
3.2. as technical context with your own infrastructure as ONE black box.
In this section one can zoom into this black box using additional
deployment diagrams:

-   UML offers deployment diagrams to express that view. Use it,
    probably with nested diagrams, when your infrastructure is more
    complex.

-   When your (hardware) stakeholders prefer other kinds of diagrams
    rather than a deployment diagram, let them use any kind that is able
    to show nodes and channels of the infrastructure.

See [Deployment View](https://docs.arc42.org/section-7/) in the arc42
documentation.

## Infrastructure Level 1 {#_infrastructure_level_1}

Describe (usually in a combination of diagrams, tables, and text):

-   distribution of a system to multiple locations, environments,
    computers, processors, .., as well as physical connections between
    them

-   important justifications or motivations for this deployment
    structure

-   quality and/or performance features of this infrastructure

-   mapping of software artifacts to elements of this infrastructure

For multiple environments or alternative deployments please copy and
adapt this section of arc42 for all relevant environments.

***\<Overview Diagram>***

Motivation

:   *\<explanation in text form>*

Quality and/or Performance Features

:   *\<explanation in text form>*

Mapping of Building Blocks to Infrastructure

:   *\<description of the mapping>*

## Infrastructure Level 2 {#_infrastructure_level_2}

Here you can include the internal structure of (some) infrastructure
elements from level 1.

Please copy the structure from level 1 for each selected element.

### *\<Infrastructure Element 1>* {#__emphasis_infrastructure_element_1_emphasis}

*\<diagram + explanation>*

### *\<Infrastructure Element 2>* {#__emphasis_infrastructure_element_2_emphasis}

*\<diagram + explanation>*

...

### *\<Infrastructure Element n>* {#__emphasis_infrastructure_element_n_emphasis}

*\<diagram + explanation>*

# 8. Cross-cutting Concepts {#section-concepts}

### 8.1 Multi-layered architecture
To achieve a sufficent separation of concerns, a common architecture which containers can implement is as follows.
This separation improves maintainability by making each layer responsible for a single concern.
```
┌──────────────────────────┐
│  Controller Layer        │  ← REST, SSE endpoints
├──────────────────────────┤
│  Service Layer           │  ← Business logic
├──────────────────────────┤
│  Repository Layer        │  ← Spring Data / custom DAO
├──────────────────────────┤
│  Model                   │  ← Entities, value objects
└──────────────────────────┘
```



::: formalpara-title
**Content**
:::

This section describes overall, principal regulations and solution ideas
that are relevant in multiple parts (= cross-cutting) of your system.
Such concepts are often related to multiple building blocks. They can
include many different topics, such as

-   models, especially domain models

-   architecture or design patterns

-   rules for using specific technology

-   principal, often technical decisions of an overarching (=
    cross-cutting) nature

-   implementation rules

::: formalpara-title
**Motivation**
:::

Concepts form the basis for *conceptual integrity* (consistency,
homogeneity) of the architecture. Thus, they are an important
contribution to achieve inner qualities of your system.

Some of these concepts cannot be assigned to individual building blocks,
e.g. security or safety.

::: formalpara-title
**Form**
:::

The form can be varied:

-   concept papers with any kind of structure

-   cross-cutting model excerpts or scenarios using notations of the
    architecture views

-   sample implementations, especially for technical concepts

-   reference to typical usage of standard frameworks (e.g. using
    Hibernate for object/relational mapping)

::: formalpara-title
**Structure**
:::

A potential (but not mandatory) structure for this section could be:

-   Domain concepts

-   User Experience concepts (UX)

-   Safety and security concepts

-   Architecture and design patterns

-   \"Under-the-hood\"

-   development concepts

-   operational concepts

Note: it might be difficult to assign individual concepts to one
specific topic on this list.

![Possible topics for crosscutting
concepts](images/08-concepts-EN.drawio.png)

See [Concepts](https://docs.arc42.org/section-8/) in the arc42
documentation.

## *\<Concept 1>* {#__emphasis_concept_1_emphasis}

*\<explanation>*

## *\<Concept 2>* {#__emphasis_concept_2_emphasis}

*\<explanation>*

...

## *\<Concept n>* {#__emphasis_concept_n_emphasis}

*\<explanation>*

# 9. Architecture Decisions

Decisions that have been made during the design of the architecture

| Decision | Status   | Description                                                                                                                                                                |
|----------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ADR-01   | accepted | Modularization Strategy: The architecture is based on a Domain-Driven Design (DDD) approach, with the exception of the UI service, which is treated as a technology layer. |

# Quality Requirements {#section-quality-scenarios}

::: formalpara-title
**Content**
:::

This section contains all quality requirements as quality tree with
scenarios. The most important ones have already been described in
section 1.2. (quality goals)

Here you can also capture quality requirements with lesser priority,
which will not create high risks when they are not fully achieved.

::: formalpara-title
**Motivation**
:::

Since quality requirements will have a lot of influence on architectural
decisions you should know for every stakeholder what is really important
to them, concrete and measurable.

See [Quality Requirements](https://docs.arc42.org/section-10/) in the
arc42 documentation.

## Quality Tree {#_quality_tree}

::: formalpara-title
**Content**
:::

The quality tree (as defined in ATAM -- Architecture Tradeoff Analysis
Method) with quality/evaluation scenarios as leafs.

::: formalpara-title
**Motivation**
:::

The tree structure with priorities provides an overview for a sometimes
large number of quality requirements.

::: formalpara-title
**Form**
:::

The quality tree is a high-level overview of the quality goals and
requirements:

-   tree-like refinement of the term \"quality\". Use \"quality\" or
    \"usefulness\" as a root

-   a mind map with quality categories as main branches

In any case the tree should include links to the scenarios of the
following section.

## Quality Scenarios {#_quality_scenarios}

::: formalpara-title
**Contents**
:::

Concretization of (sometimes vague or implicit) quality requirements
using (quality) scenarios.

These scenarios describe what should happen when a stimulus arrives at
the system.

For architects, two kinds of scenarios are important:

-   Usage scenarios (also called application scenarios or use case
    scenarios) describe the system's runtime reaction to a certain
    stimulus. This also includes scenarios that describe the system's
    efficiency or performance. Example: The system reacts to a user's
    request within one second.

-   Change scenarios describe a modification of the system or of its
    immediate environment. Example: Additional functionality is
    implemented or requirements for a quality attribute change.

::: formalpara-title
**Motivation**
:::

Scenarios make quality requirements concrete and allow to more easily
measure or decide whether they are fulfilled.

Especially when you want to assess your architecture using methods like
ATAM you need to describe your quality goals (from section 1.2) more
precisely down to a level of scenarios that can be discussed and
evaluated.

::: formalpara-title
**Form**
:::

Tabular or free form text.

# 10. Risks and Technical Debts {#section-technical-risks}

::: formalpara-title
**Contents**
:::

A list of identified technical risks or technical debts, ordered by
priority

::: formalpara-title
**Motivation**
:::

"Risk management is project management for grown-ups" (Tim Lister,
Atlantic Systems Guild.)

This should be your motto for systematic detection and evaluation of
risks and technical debts in the architecture, which will be needed by
management stakeholders (e.g. project managers, product owners) as part
of the overall risk analysis and measurement planning.

::: formalpara-title
**Form**
:::

List of risks and/or technical debts, probably including suggested
measures to minimize, mitigate or avoid risks or reduce technical debts.

See [Risks and Technical Debt](https://docs.arc42.org/section-11/) in
the arc42 documentation.

# 11. Glossary {#section-glossary}

::: formalpara-title
**Contents**
:::

The most important domain and technical terms that your stakeholders use
when discussing the system.

::: formalpara-title
**Motivation**
:::

| Term                  | Definition                                                                                          |
|-----------------------|-----------------------------------------------------------------------------------------------------|
| WYSIWYG               | What you see is what you get. Further information: [WYSIWYG](https://en.wikipedia.org/wiki/WYSIWYG) |
| MCP                   | Model Context Protocol. Further information: [MCP](https://modelcontextprotocol.io/introduction)    |