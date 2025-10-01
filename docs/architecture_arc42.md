
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


### 1.4.1 User requirements

| ID    | Requirements                                                                                                                                                                                                                                                                                                                                                                                     |
|-------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| UR_01 | As a member of the system, I want to be able to create and use my personal tools. As well as tools that are associated with a group that I am part of.                                                                                                                                                                                                                                           |
| UR_02 | As a member of the system, I want to be able to view the group profile of a public group, join a public group and leave a group that I am part of.                                                                                                                                                                                                                                               |
| UR_03 | As a member of the system, I want to edit my personal profile. I want to be able to edit my name, password and profile picture.                                                                                                                                                                                                                                                                  |
| UR_04 | As a member of a group, I want to use all tools that are associated with the group.                                                                                                                                                                                                                                                                                                              |
| UR_05 | As a member of a group, I want to be able to leave the group.                                                                                                                                                                                                                                                                                                                                    |
| UR_06 | As a member of a group, I want to be able to view the group profile                                                                                                                                                                                                                                                                                                                              |
| UR_07 | As an editor of a group, I want to perform CRUD operations for all tools that belong to the group.                                                                                                                                                                                                                                                                                               |
| UR_08 | As an administrator of a group, I want to perform CRUD operations for all tools that belong to the group.                                                                                                                                                                                                                                                                                        |
| UR_09 | As an administrator of a group, I want to edit the group profile. I want to be able to change the name, description and visibility of the group. A group can either be public or private. A public group can be joined from any user of the system. The members of a private group can only be managed by an administrator.                                                                      |
| UR_10 | As an administrator of a group, I want to change the role of a member of this group. A member can either be a normal member, editor or administrator.                                                                                                                                                                                                                                            |
| UR_11 | As an administrator of a group, I want to add and remove members from this group.                                                                                                                                                                                                                                                                                                                |
| UR_12 | As an administrator of the system, I want to perform CRUD operations for all users of the system. Additionally I want to assign a system role to a user. A user can either be a member or an administrator of the system.                                                                                                                                                                        |
| UR_13 | As an administrator of the system, I want to perform CRUD operations for all groups of the system. Additionally I want to edit the group profile, add and remove users from a specific group, or change their role within that group.                                                                                                                                                            |
| UR_14 | As an administrator of the system, I want to perform CRUD operations for all tools registered in the system. These can either be personal tools from a user or tools associated with a group.                                                                                                                                                                                                    |
| UR_15 | As a user of the system with an account, I want to log in to my account using my name and password. Functions are provided depending on user role/ rights.                                                                                                                                                                                                                                       |
| UR_16 | As a user of the system without an account, I want to register myself using a name, password and also like to be able to add a profile picture.                                                                                                                                                                                                                                                  |
| UR_17 | As a user of the system, I want to view and use all my tools through a single MCP server with any AI agent that supports MCP.                                                                                                                                                                                                                                                                    |
| UR_18 | As a user of the system, I want to access all of its functions through an intuitive UI and an API.                                                                                                                                                                                                                                                                                               |
| UR_19 | As a user of the system, I want to be able to generate a new MCP tool. For this I want to be able to upload an API specification, provide the root endpoint url, authentication/ authorization information, a name, description and version. If the API specification already includes some of the additional information I want to automatically fill the input fields to simplify the process. |
| UR_20 | The system should support all common API specifications like OpenAPI, RAML, etc.                                                                                                                                                                                                                                                                                                                 |

### 1.4.2 Functional Requirements

#### ID Structure

SR_XX_YY:
SR -> System requirement
XX -> Group/ Micro service
YY -> sequential unique id

Groups
UI -> User Interface
UsM -> UserManagement
Api -> ApiManagement
Mcp -> McpManagement
S2T -> Spec2Tool
Arc -> Architecture
Mai -> Maintainability
Sec -> Security


| ID        | Requirement                                                                                                                                                                                                                                                                                                                                                                                                                                   | Reference                                                                          | Dependency |
|-----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|------------|
| SR_Arc_01 | The system SHALL be composed of multiple domain-specific microservices (McpManagement, ApiManagement, UserManagement, Spec2Tool).                                                                                                                                                                                                                                                                                                             |                                                                                    |            |
| SR_Arc_02 | Microsservices SHALL be written in Java Spring and communicate with each other via internal RESTful APIs.                                                                                                                                                                                                                                                                                                                                     |                                                                                    |            |
| SR_Arc_03 | The UI SHALL be a separate service build with React.                                                                                                                                                                                                                                                                                                                                                                                          |                                                                                    |            |
| SR_Arc_04 | Each microservice except the UI SHALL be composed of a layered architecture (Controller, Service, Repository).                                                                                                                                                                                                                                                                                                                                |                                                                                    |            |
| SR_Arc_05 | Each service SHALL be deployed on a THM server as a docker container.                                                                                                                                                                                                                                                                                                                                                                         |                                                                                    |            |
| SR_Sec_01 | All passwords SHALL be stored in a hashed format.                                                                                                                                                                                                                                                                                                                                                                                             |                                                                                    |            |
| SR_Sec_02 | All API tokens SHALL be stored in a base64 coded format.                                                                                                                                                                                                                                                                                                                                                                                      |                                                                                    |            |
| SR_Sec_03 | The system SHALL use OAuth 2.0 access tokens to authorize access to protected resources based on the user's system role and group roles. The roles SHALL be stored inside the system itself.                                                                                                                                                                                                                                                  |                                                                                    |            |
| SR_Sec_04 | The system SHALL delegate all authentication to a dedicated Keycloak server.                                                                                                                                                                                                                                                                                                                                                                  |                                                                                    |            |
| SR_Mcp_01 | When a logged in user wants to add a new tool, the McpManagement service MUST allow to add a new tool by providing an id from the ApiManagement and a GroMoKoSo tool specification.                                                                                                                                                                                                                                                           |                                                                                    |            |
| SR_Mcp_02 | When a logged in user wants to delete an existing tool, the McpManagement service MUST allow to delete a tool by providing its id.                                                                                                                                                                                                                                                                                                            |                                                                                    |            |
| SR_Mcp_03 | When a new tool is added to the system, the tool id MUST be equal to the id from the ApiManagement.                                                                                                                                                                                                                                                                                                                                           |                                                                                    |            |
| SR_Mcp_04 | When the tools list from a user has changed, the McpManagement service MUST allow other services to notify the McpManagement about these changes.                                                                                                                                                                                                                                                                                             |                                                                                    |            |
| SR_Mcp_05 | The McpManagement MUST persist all tool specifications in a SQLite database.                                                                                                                                                                                                                                                                                                                                                                  |                                                                                    |            |
| SR_Mcp_06 | When a user/ service calls an API endpoint, the requester MUST authorize each request using an Access Token provided by the Keycloak server.                                                                                                                                                                                                                                                                                                  |                                                                                    |            |
| SR_Mcp_07 | The McpManagement MUST provide a single MCP server endpoint for AI agents to discover and use all tools associated with a specific user.                                                                                                                                                                                                                                                                                                      |                                                                                    |            |
| SR_Mcp_08 | When an AI agent calls the MCP endpoint, the system MUST restrict a user's access to only their own personal tools and the group tools of the groups they are a member of.                                                                                                                                                                                                                                                                    |                                                                                    |            |
| SR_Mcp_09 | When an AI agent without a valid access token calls any MCP endpoint, the system MUST return a 401 Unauthorized Http Error and return a www-authenticate bearer header with the corresponding resource_metadata property according to RFC 9728.                                                                                                                                                                                               |                                                                                    |            |
| SR_Api_01 | The ApiManagement MUST represent APIs as an entity with the following attributes: id (Primary Key), name, description, version, dataFormat, spec, token (Base64 encoded).                                                                                                                                                                                                                                                                     | SR_Sec_02                                                                          |            |
| SR_Api_02 | The ApiManagement MUST persist APIs inside a SQLite database.                                                                                                                                                                                                                                                                                                                                                                                 |                                                                                    |            |
| SR_Api_03 | The ApiManagement MUST offer CRUD operations for APIs as a RESTful API.                                                                                                                                                                                                                                                                                                                                                                       |                                                                                    |            |
| SR_Api_04 | The ApiManagement MUST use UserManagement to determine if CRUD operations should be allowed.                                                                                                                                                                                                                                                                                                                                                  |                                                                                    |            |
| SR_Api_04 | The ApiManagement MUST update the tools inside MCPManagement whenever a tool is created or modified.                                                                                                                                                                                                                                                                                                                                          | SR_Mcp_03, SR_Mcp_01                                                               |            |
| SR_Api_05 | The ApiManagement MUST use Spec2Tool to convert OpenAPI specifications to an internal TooDefinition.                                                                                                                                                                                                                                                                                                                                          |                                                                                    |            |
| SR_Api_06 | A ToolDefinition SHALL consist of the following attributes: name: string, description: string, tools= [{ name: string, description: string, endpoint: string, parameter= [{ name: string, description: string, type: string }] }]                                                                                                                                                                                                             | SR_Api_05                                                                          |            |
| SR_Api_06 | The ApiManagement MUST provide a way to invoke requests to present external APIs and return its response.                                                                                                                                                                                                                                                                                                                                     |                                                                                    |            |
| SR_Api_07 | ApiManagement MUST be able to execute http requests with different http Methods (Post, Put, Get, Patch, Delete), a specified url with placeholders, a http header, a body as well as request params and values for placeholders inside the url.                                                                                                                                                                                               | SR_Api_06                                                                          |            |
| SR_Api_08 | ApiManagement MUST return the response of external APIs in the following structure: responseCode (4xx, 2xx, etc.), header, body.                                                                                                                                                                                                                                                                                                              | SR_Api_06                                                                          |            |
| SR_UsM_01 | When a logged in System Admin wants to edit user related data, the UserManagement MUST be able to create, edit and persist data of a User entity. This data consists of: Username, Firstname, Lastname, E-Mail, Active and Inactive API IDs, System Role, which can be: Member, SysAdmin.                                                                                                                                                     | UR_01, UR_03, UR_12                                                                |            |
| SR_UsM_02 | When a logged in System Admin or Group Admin wants to edit group related data within the corresponding group, the UserManagement MUST be able to create, edit and persist data of a Group entity. This data consists of: Groupname, Description, Users which are part of the group, User roles within the group which can be: Group Member, Group Editor, Group Admin, Active and Inactive API IDs, Grouptype, which can be: Private, Public. | UR_01, UR_02, UR_04, UR_05, UR_07, UR_08, UR_09, UR_10, UR_11, UR_13, UR_15, UR_16 |            |

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


## 2.3 Conventions
Programming Conventions are documented on the wiki page in Gitlab. And can be found [here](https://git.thm.de/softwarearchitektur-wz-ss24/studentswa2025/enton/gromokoso/-/wikis/home/conventions).


# 3. Context and Scope

The context boundary represents the `GroMoKoSo` system in relation to its external interfaces, users, and neighboring systems.
The goal of this chapter is to make the system\'s communication relationships with its environment transparent.

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
    - API Id\'s which the user has access to and whether they are currently active.
- Group data:
  - Unique name of the group.
  - Users which can have the following roles within the group:
    - Group member
    - Group editor
    - Group admin
  - API Id\'s which the users of the group have access to.
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

![User interface component view](/docs/diagrams/level_3_component/user_interface_container_component_view_excalidraw.svg)

| Element     | Description                                         |
|-------------|-----------------------------------------------------|
| Router      | React component that handles routing                |
| Page        | React component that can ge accessed throug a route |


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

| Element        | Description                                                                                                                 |
|----------------|-----------------------------------------------------------------------------------------------------------------------------|
| Mcp Client     | Mcp client that confirms to the [mcp specification](https://modelcontextprotocol.io/specification/2025-06-18/client/roots)  |
| ApiManagement  | see chapter [5.2.3](#523-apimanagement)                                                                                     |
| UserManagement | see chapter [5.2.2](#522-usermanagement)                                                                                    |
| ToolDb         | SQL Database storing tool definitions                                                                                       |
| Controller     | (REST) Endpoints for external systems                                                                                       |
| Service        | Business logic/ Mcp servers                                                                                                 |
| Entity         | Entities (ORM), value objects                                                                                               |
| Repository     | Encapsulate persistence                                                                                                     |
| Security       | Cross-cutting concern: OAuth2 authorization                                                                                 |
| Configuration  | Cross-cutting concern: Spring configuration classes                                                                         |
| Client         | Cross-cutting concern: Communication with other subsystems (ApiManagement, UserManagement)                                  |

The class diagram can be found [here](/docs/diagrams/level_4_class/mcp_management_class_diagram.drawio)

### 5.3.5 Spec2Tool
The Spec2Tool Microservice implements a layer architecture to cleanly separate the different concerns.
It does not contain any repository or entity classes since it does not persistent any data.

![Spec 2 Tool Components](/docs/diagrams/level_3_component/spec2tool_container_component_view.svg)

| Element    | Description                                                                             |
|------------|-----------------------------------------------------------------------------------------|
| Controller | (REST) Endpoints for external systems                                                   |
| Service    | Business logic which will call the correct mapper class depending on given file format. |
| Mapper     | Concrete classes which are responsible to convert specification to tool representation. |

# 6. Runtime View {#section-runtime-view}

## Add new MCP Tool
** Scenario**: A user wants to add a new MCP tool for his client.

![Sequence diagram add new MCP tool](/docs/diagrams/runtime/add_mcp_tool.png)

1. The User Interface send the entered data to the API Management.
2. The API Management requests the User Management to save the new API to the corresponding user with the active status.
3. The User Management signals that the data was saved.
4. The API Management request Spec2Tool to convert the given API definition to an internal Tool definition.
5. The Spec2Tool returns the requested data format.
6. The API Management requests the MCP Management to add the new Tool to the toolset of the corresponding user.
7. The MCP Management signals that the tool was registered.
8. API Management signals the User Interface that the workflow was successfully performed. 

## List existing MCP Tools
**Scenario**: A user wants to list the available tools of his MCP client.  

![Sequence diagram list existing MCP tools](/docs/diagrams/runtime/list_existing_tools.png)

1. The MCP clients gets the order to list the available tools.
2. A GET Request is sent to the User Management asking for the all API IDs which are available for the user with the given ID.
3. The Object API_ACCESS which is returned is a list of API IDs with the additional information if the corresponding tool is set as active and if it is available to the user via a group or personal.
4. For each API ID the corresponding spec is requested from the ApiManagement.
5. The spec gets returned.
6. Depending on whether they are active or not they will be shown differently.
7. --
8. --
9. --

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

# 7. Deployment View


TODO: Justin

# 8. Cross-cutting Concepts

## 8.1 Multi-layered architecture
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

TODO: Keycloak as a central authentication provider


# 9. Architecture Decisions

Decisions that have been made during the design of the architecture

| Decision                                          | Status   | Description                                                                                                                                                                |
|---------------------------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [ADR-01](./adr/adr_01_modularization.md)          | accepted | Modularization Strategy: The architecture is based on a Domain-Driven Design (DDD) approach, with the exception of the UI service, which is treated as a technology layer. |
| [ADR-02](./adr/adr_02_mcp_server.md)              | accepted | MCP Server Authorization and User Tool Mapping: Each user gets their own MCP server instance to handle authorization and tool mapping.                                     |
| [ADR-03](./adr/adr_03_mcp_transport_protocol.md)  | accepted | MCP Transport Protocol: Use the Streamable HTTP transport protocol instead of the deprecated SSE to avoid proxy issues with long-lasting connections.                      |


# 10. Risks and Technical Debts

TODO: better architecture/ microservice separation

## 10.1 Poorly Separated Microservices
Our initial design, which aimed for a strict Domain-Driven Design (DDD) separation of microservices,
has led to an unanticipated level of inter-service dependency. 
This has resulted in a situation where a single user request often requires calls to all other microservices to complete.
This chain of dependencies poses significant risks and is a source of technical debt.

### Risks
- High Latency and Performance Degradation: 
  The numerous network calls required for a single request introduce significant latency.
  This degrades the overall performance and user experience.
- Increased Risk of Failure:
  A single point of failure in any of the dependent services can cause the entire request to fail.
  The high number of dependencies exponentially increases this risk, making the system less robust and reliable.
- Complex Rollbacks:
  To maintain data integrity, we must perform complex rollbacks and compensation logic when a request fails mid-transaction.
  This is a significant operational burden and introduces complexity that could lead to further errors.
- Tight Coupling:
  The services are tightly coupled despite the microservice architecture's goal of loose coupling.
  This makes it difficult to deploy, scale, and maintain services independently.

### Mitigating Technical Debt
To address this technical debt, we have implemented a Command Pattern approach to manage transactional integrity. 
This allows us to undo changes made by successfully completed requests if a subsequent request in the chain fails.

> [!IMPORTANT]
> This is a **temporary solution** to mitigate the immediate risk of data inconsistency.

Additionally, we have developed a Proof of Concept (PoC) for a new architectural design.
The new PoC aims to re-evaluate the service boundaries and create a more cohesive and loosely coupled architecture.
We plan to refactor our services based on this new design, 
which will involve grouping related functionalities to reduce the number of cross-service calls and eliminate tight coupling.
This is our long-term strategy for addressing this critical technical debt.

![Proof-of-Concept: New Architecture](/docs/diagrams/proof_of_concept_new_architecture.svg)

TODO: better tool management


# 11. Glossary

| Term                         | Definition                                                                                                                                                                            |
|------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| WYSIWYG                      | What you see is what you get. Further information: [WYSIWYG](https://en.wikipedia.org/wiki/WYSIWYG)                                                                                   |
| MCP                          | Model Context Protocol. Further information: [MCP](https://modelcontextprotocol.io/introduction)                                                                                      |
| tool                         | A set of MCP tools that combined represent the MCP equivalent of all endpoints from an API specification.                                                                             |
| avatar                       | Profile picture.                                                                                                                                                                      |
| group role                   | Role that gives a user special permissions only inside the group.                                                                                                                     |
| system role                  | Role that gives a user special permissions system-wide.                                                                                                                               |
| group member                 | A user that is part of a group. Not to be confused with the system role or group role “member.”                                                                                       |
| personal tool                | Tool that belongs to a specific user. Only this user can use and manage this tool.                                                                                                    |
| group tool                   | Tool that is associated with a group. A group tool does not belong to a specific user. Instead, it belongs to the group. Any user that is a member of the group can use a group tool. |
| GroMoKoSo tool specification | Specification used internally to describe a tool. The system allows to transform the most common API specifications to this internal specification.                                   |
| user                         | A person with any role that uses the GroMoKoSo system in any way.                                                                                                                     |
