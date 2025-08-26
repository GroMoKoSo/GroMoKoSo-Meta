# ADR-02 MCP Server Authorization and User Tool Mapping

| Status           | accepted                     |
|------------------|------------------------------|
| Decision makers  | Josia Menger, Jannik Heimann |
| Reviewer         | GroMoKoSo Team               |
| Date of Decision | 18-08-2025                   |

## Context

### Given
- The GroMoKoSo system MUST be secured using OAuth2 (via given KeyCloak authorization server)
- The system manages MCP tools which are exposed through an MCP server.
- The system manages user and groups. A user can be part of zero or more groups.
- Each user and each group has a list of tools. A user can access both its personal tools and those provided by a group.

Simplified an MCP Server knows two different request for tools:
1. Get list of available tools (GET /sse)
2. Invoke a tool (POST /mcp/message)

As part of the feasibility study at the beginning of the project, the actual calling of the tools was already explored in detail.
[TODO: Maybe write a short summary about the findings!]

### Derived requirements:
1. The McpManagement MUST authenticate the MCP client using the authorization server (KeyCloak) when a user connects to the MCP server through an MCP client.
2. The McpManagement MUST check if the current user is authorized to use a tool when a user invokes an MCP tool through an MCP client.
3. The McpManagement MUST return only the tools available to the requesting user when a user queries the available MCP tools through an MCP client.

### Problem
In the MCP management we need to map the tools to the user and only provide the user with the appropriate tools.
To archive this behavior the service must consider the access token sent with an incoming mcp request.
Based on the user for which this token was issued the response must be adjusted.
Additionally, multiple users with different tool lists can be connected to the server at the same time.

The first request introduces a way bigger problem.
Normally Spring handles the request completely automatically without any option to manipulate the request.
To overcome this problem an in depth analysis was performed to evaluate multiple different solutions.

## Considered Options

### Option 1: Override part of the McpServer implementation to include custom logic

The complete system only uses the autoconfigured MCP server provided by the spring starter mcp server dependency.
To handle different users with different tool lists we need to override part of the mcp server logic to handle tools per session instead of per instance.
So every client (user) that connects to the system gets its own session with its own tool list.

To implement the first solution, we first need to find a suitable place where we can manipulate the McpServer from the outside.
Therefor we first need to understand how the McpServer works under the hood.

The article [Dynamic Tool Updates in Springs AI's Model Context Protocol](https://spring.io/blog/2025/05/04/spring-ai-dynamic-tool-updates-with-mcp)
suggests a way to dynamically add tools at runtime based on the current user context, but it doesn't go into more detail where and how to implement this.
To gain more insides we have to dig into the actual spring MCP server code (A more or less detailed research can be found in [chapter research](#research)).

In theory, we can switch out the factory to provide a session customized to the user that is requesting the connection initialization.
One Problem remains: How can we add/ remove tools on runtime and notify the connected clients?
We properly need to build our own implementation of an MCP Server which manages tools per session instead of per instance.
This would involve also reimplementing all unchanged features of the MCP Server.

Alternatively we change the original implementation of the spring MCP server library and manually recompile the library with our changes.

### Option 2: Each user gets its own mcp server

Each user uses a different instance of an MCP server. So naturally each user has its own tools.

To dynamically react to new users we need to create the MCP server instances on runtime and cannot rely on the autoconfigured MCP server.

Another problem is the routing of the client requests to the correct MCP server.
The autoconfiguration MCP server uses a configuration bean to register an RouterFunction during initialization.
Since we are creating MCP server instances on runtime we also need to register the MCP server during runtime.

Things to look out for:
- Maybe then every user needs its own endpoint for its personal MCP server?
- Maybe it is even possible to redirect request from a central endpoint to an alternativ endpoint using HTTP Error code 301 Moved Permanently or similar.

## Decision

After extensive research and a longer discussion with Jannik we decided to go with Option 2.

With the current implementation of an MCP Server in Spring I see no way how a single MCP server instance can handle multiple users with different tools, etc.
We cannot change the server state on runtime for every request.
This would also cause a lot of problem automatically updating all connected client about configuration changes.
The only way the single server option would work, if every users gets every registered tool advertised.
If a client than tries to use a tool that is not intended for that user, an error would be thrown and the request would fail.
This results in a really poor user experience, maybe even lead to that some user can’t use the service because other tools from other users overlap with their own tool.
Or we need to completely reimplement the full MCP server.

A complete analysis can be found in the following chapter [Research](#research).

## Consequences

Each user should get its own server which significantly decreases the complexity for the tools and authorization implementation.
The MCP servers are stored inside a central manager instance and request are routed to the corresponding server via a RouterFunction.
The router is registered at startup and first delegates the request to a handler, which can then select the appropriate server via the manager class.

## Research

Test setup:
- KeyCloak Server running in a docker container on localhost:9000
- Spring test application running on localhost:8080 acting as an MCP server (WebMVC)
- VS Code with GitHub Copilot as an MPC client


### How to secure a Spring application with OAuth 2.0?

OAuth generally defines four different roles:
> - **Resource Owner**: The user or system that owns the protected resources and can grant access to them.
> - **Client**: The client is the system that requires access to the protected resources. To access resources, the Client must hold the appropriate Access Token.
> - **Authorization Server**: This server receives requests from the Client for Access Tokens and issues them upon successful authentication and consent by the Resource Owner. The authorization server exposes two endpoints: the Authorization endpoint, which handles the interactive authentication and consent of the user, and the Token endpoint, which is involved in a machine to machine interaction.
> - **Resource Server**: A server that protects the user’s resources and receives access requests from the Client. It accepts and validates an Access Token from the Client and returns the appropriate resources to it.
    [^1]

The McpManagement and every other service form the GroMoKoSo System acts as a resource server and/or as a client.
KeyCloak represents the authorization server for each service.

To secure an application simply add the `spring-boot-starter-oauth2-resource-server` dependency to a project.
The only thing that needs to be configured in the `application.properties` is the issuer uri.
The uri needs to point to the root path of the authorization server.
In the case of KeyCloak this uri is `ip/realms/MyRealm`

application.properties
```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9000/realms/GroMoKoSo
```

application.yaml
```yaml
springs:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: "http://localhost:9000/realms/GroMoKoSo"
```

The neat part about this setup is that the MCP server is already automatically secured too!

If now any endpoint of the application gets invoked an HTTP 401 Unauthorized error is returned with a `www-authenticate` header.
This header includes the keyword `Bearer` which indicates that a Bearer Token is required to access this endpoint.

By default, Spring only uses the iss claim inside the JWT Token to authorise client requests and secures every endpoint:

> Where `idp.example.com/issuer` is the value contained in the `iss` claim for JWT tokens that the authorization server will issue. Resource Server will use this property to further self-configure, discover the authorization server’s public keys, and subsequently validate incoming JWTs.
[^6]

To configure the authorization in more detail a `SecurityFilterChain` can be used like this [^8]:

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Protect all endpoints with OAuth2
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )

                // Enable OAuth2 Resource Server support
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(Customizer.withDefaults())
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
```

### WWW-Authenticate

Defined by [RFC 6750](https://datatracker.ietf.org/doc/html/rfc6750#section-3)

`www-authenticate: Bearer realm=””, scope””, error=””, error_description=””`

All attributes of the challenge are optional and can be omitted.

bearer ⇒ german: Träger, Inhaber

Definition of `bearer token` as described in chapter 1.2 Terminology
> A security token with the property that any party in possession of
the token (a "bearer") can use the token in any way that any other
party in possession of it can.  Using a bearer token does not
require a bearer to prove possession of cryptographic key material
(proof-of-possession).

### How to authenticate the user using an MCP client?

The Model Context Specification defines the authorization server discover process but doesn't cover every detail.
Instead, the relevant RFCs often have to be consulted for more details:

To allow for automatic authorization
> MCP servers **MUST** implement the OAuth 2.0 Protected Resource Metadata (**RFC9728**) specification to indicate the locations of authorization servers.
> The Protected Resource Metadata document returned by the MCP server **MUST** include the `authorization_servers` field containing at least one authorization server.
[^9]

The RFC defines that the resource metadata should be found at a well known url:
> A protected resource metadata document **MUST** be queried using an HTTP GET request at the previously specified URL.
> The consumer of the metadata would make the following request when the resource identifier is https://resource.example.com and the well-known URI path suffix is oauth-protected-resource to obtain the metadata,
> since the resource identifier contains no path component:
>
> GET /.well-known/oauth-protected-resource HTTP/1.1<br>
> Host: resource.example.com
[^7]

It also defines what the response needs to contain:
> The response is a set of metadata parameters about the protected resource's configuration.
> A successful response **MUST** use the 200 OK HTTP status code and return a JSON object using the application/json content type that contains a set of metadata parameters as its members that are a subset of the metadata parameters defined in Section 2.
> Additional metadata parameters **MAY** be defined and used;
> any metadata parameters that are not understood **MUST** be ignored.
> \[...]
> ```
> HTTP/1.1 200 OK
> Content-Type: application/json
> 
> {
>   "resource":
>       "https://resource.example.com",
>   "authorization_servers":
>       ["https://as1.example.com",
>       "https://as2.example.net"],
>   "bearer_methods_supported":
>       ["header", "body"],
>   "scopes_supported":
>       ["profile", "email", "phone"],
>   "resource_documentation":
>       "https://resource.example.com/resource_documentation.html"
> }
> ```
> [^7]

To implement this feature in Spring we need to create a RestController that exposes the information we need.
In our case the only important parameters are the mandatory fields `resource` and `authorization_servers`.
Here it is important that the `authorization_servers` field has a string array as a value.
```java
@RestController()
public class WellKnownMetadataController {

    @GetMapping(".well-known/oauth-protected-resource")
    public Map<String, Object> getProtectedResourceMetadata() {
        return Map.of(
                "resource", "http://localhost:8080/sse",
                "authorization_servers", new String[]{"http://localhost:9000/realms/GroMoKoSo"}
        );
    }
}
```

We also need to configure spring security so that this endpoint is not protected through OAuth.

To allow the MCP client to automatically find this metadata we somehow need to inform the client where to search for the data.
This can be done using the `www-authenticate` header in an error response:
> MCP servers **MUST** use the HTTP header `WWW-Authenticate` when returning a 401 Unauthorized to indicate the location of the resource server metadata URL as described in **RFC9728 Section 5.1 “WWW-Authenticate Response**”.
[^9]

Looking into the RFC we can found the needed parameter:
> \[...] [T]he WWW-Authenticate HTTP response header field to indicate the protected resource metadata URL:
>
> resource_metadata:<br>
> The URL of the protected resource metadata.
[^7]

To implement this functionality in Spring we need to create a custom `AuthenticationEntryPoint`:

```java
@Component
public class OAuthChallengeEntryPoint implements AuthenticationEntryPoint {

    private static final String REALM = "GroMoKoSo";
    private static final String RESOURCE_METADATA = "http://localhost:8080/.well-known/oauth-protected-resource";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String challenge = String.format(
                "Bearer realm=\"%s\", error=\"%s\", error_description=\"%s\", resource_metadata=\"%s\"",
                REALM,
                "invalid_token",
                authException.getMessage(),
                RESOURCE_METADATA
        );
        response.setHeader("WWW-Authenticate", challenge);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\", "
                + "\"message\": \"" + authException.getMessage() + "\"}");
    }
}
```

To register that challenge point we need to add another method call in the SecurityFilterChain

```java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ...
                // Include redirects to the authorization server in the challenge response
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(challengeEntryPoint));
        return http.build();
    }
```

#### MCP client OAuth 2.0 authentication sequence

![MCP client OAuth 2.0 authentication sequence diagram](/docs/diagrams/runtime/mcp_management/mcp_client_oauth2.svg)

1. Request protected resource (GET /sse)
2. Response 401 Unauthorised with www-authenticate bearer
3. Client extracts resource metadata uri form www-authenticate header
4. Client fetches metadata from resource server (GET /.well-known/oauth-protected-resource) as defined in section 3.1. [^7]
5. Client extracts authorization server uri from metadata
6. Client fetches metadata from authorization server (GET /.well-known/oauth-authorization-server)
7. Client uses authorization endpoint to get Authorization Code for an token using the Authorization Code Grant with Proof Key for Code Exchange (PKCE)
8. Authorization server returns Authorization Code
9. Client exchanges authorization code for an Access Token using the token endpoint
10. Authorization server returns Access Token
11. Client requests protected resource with valid Access Token
12. Ressource server response with resource content


### How to check that a user is authorized to call a tool?

For the "Invoke a tool"-request this is relative straight forward.
We already have to create the tool handlers from scratch to support our own custom tool behavior.
So in theorie we can just include a condition that checks if the user is authorized.

Method to create a new MCP tool on runtime:
```java
private McpServerFeatures.AsyncToolSpecification getToolSpecification(String name, String description, String inputSchema,
                     Function<Object[], Object> toolFunction) {
        return new McpServerFeatures.AsyncToolSpecification(
                new McpSchema.Tool(
                        name,
                        description,
                        inputSchema
                ),
                (exchange, args) -> {
                    try {
                        // if user is not authorized return HTTP 401 Unauthorized 
                        String callResult = toolFunction.apply().toString();
                        return Mono.fromSupplier(() -> new McpSchema.CallToolResult(
                                List.of(new McpSchema.TextContent(callResult)), false));
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        return Mono.fromSupplier(() ->new McpSchema.CallToolResult(
                                List.of(new McpSchema.TextContent(e.getMessage())), true));
                    } finally {
                        OAuthTokenHolder.clear();
                    }
                }
        );
    }
```

Currently, however, Spring has a bug, which is why all information about the authorization is missing inside a tool call.
The related bug report can also be found here: [MCP server: Authentication lost in tool execution](https://github.com/spring-projects/spring-ai/issues/2506)

It's possible to work around this problem. However, the workaround introduces a potential security vulnerability.
With the workaround the access token is provided via a static field that can be read and modified by any class.
This could lead to unwanted token leaks.

Class with static field to store the token on runtime.
```java
public class OAuthTokenHolder {
    private static final InheritableThreadLocal<String> holder = new InheritableThreadLocal<>();

    public static void set(String token) {
        holder.set(token);
    }

    public static String get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}
```

Get the token from the security context and validate that the token is JWT token.
```java
public record AccessTokenExtractor(SecurityContext securityContext) {

    public Jwt extractAccessToken() {
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("Authentication is not a JwtAuthenticationToken");
        }
        return jwtAuth.getToken();
    }
}
```

Register a hook to extract the access token when a new user makes a request
```java
@Configuration
public class ReactorElasticBoundConfig {

    private static final Logger logger = LoggerFactory.getLogger(ReactorElasticBoundConfig.class);

    @PostConstruct
    public void init() {
        Function<Runnable, Runnable> decorator = runnable -> {
            Jwt token = new AccessTokenExtractor(SecurityContextHolder.getContext())
                    .extractAccessToken();

            return () -> {
                try {
                    OAuthTokenHolder.set(token.getTokenValue());
                    runnable.run();
                } finally {
                    logger.info("[{}] [Reactor Hook] McpUserHolder clean",
                            Thread.currentThread().getName());
                    OAuthTokenHolder.clear();
                }
            };
        };

        Schedulers.onScheduleHook("McpBoundedElasticHook", decorator);
    }
}
```

### How does the automatically configured MCP server work and how can it be disabled?

The `spring-ai-starter-mcp-server-webmvc` dependency, like any other starter dependency,
is essentially just a list of all the dependencies required to build and run an MCP server.

- spring-boot-starter
- spring-ai-autoconfigure-mcp-server
- spring-ai-mcp
- mcp-spring-webmvc
- spring-boot-starter-web

The important dependency for the automatic configuration is `spring-ai-autoconfigure-mcp-server`

The automatic configuration can be simply disabled with a config that can be added to the application properties (yaml).

application.properties
```properties
spring.ai.mcp.server.enabled=false
```

application.yaml
```yaml
spring:
  ai:
    mcp:
      server:
        enabled: false
```

### How to create and register an MCP server on runtime?

First we need to understand how an MCP server functions under the hood:

- The MCP Sync Server is just a wrapper for the Async Server.
  Every request is done using the async methods while blocking the main thread.
- The Async Server stores the tools inside the private attribute `tools`

io.modelcontextprotocol.server.AsyncMcpServer:95
```java
private final CopyOnWriteArrayList<McpServerFeatures.AsyncToolSpecification> tools = new CopyOnWriteArrayList<>();
```

- On startup the server registers a request handler for tool listing and tool calling.

io.modelcontextprotocol.server.AsyncMcpServer:142
```java
// Add tools API handlers if the tool capability is enabled
if (this.serverCapabilities.tools() != null) {
	requestHandlers.put(McpSchema.METHOD_TOOLS_LIST, toolsListRequestHandler());
	requestHandlers.put(McpSchema.METHOD_TOOLS_CALL, toolsCallRequestHandler());
}
```

- The provided callback methods are also private members of the class.
  So here we cannot simply create a child class that override this implementation.

io.modelcontextprotocol.server.AsyncMcpServer:341
```java
	private McpServerSession.RequestHandler<McpSchema.ListToolsResult> toolsListRequestHandler() {
		return (exchange, params) -> {
			List<Tool> tools = this.tools.stream().map(McpServerFeatures.AsyncToolSpecification::tool).toList();

			return Mono.just(new McpSchema.ListToolsResult(tools, null));
		};
	}
```

- Another aspect I looked into, is the request handler array itself. The array is stored inside an
  `McpServerSession`. This class holds the request handler inside a private array with no public getter or setter.

Some more possible placed that we can look into, to check if they provide an easy way to override functionality: `McpTransportProvider`

- The `McpTransportProvider` interface provides three implementation: stdio, sse/http with servlet and sse with webflux.
- For each connection the `McpTransportProvider` creates a new `McpSession` using the `McpSession.Factory` and stores it inside a private attribute.
- For every request the `McpTransportProvider` looks for the correct session and calls the callbacks that are stored inside the `McpSession`
- The factory that the `McpTransportProvider` uses to create new sessions is set using a public setter `setSessionFactory`

So to build the server you need a `McpServerTransportProvider` and `McpSchema.ServerCapabilites`.
Then you can use one of the builders provided by the `McpServer` class to create a sync or async server.

```java
 WebMvcSseServerTransportProvider provider = new WebMvcSseServerTransportProvider(new ObjectMapper(),
                "/mcp/message", "/sse");

        McpSchema.ServerCapabilities capabilities = McpSchema.ServerCapabilities.builder()
                .tools(true)
                .logging()
                .build();

        McpAsyncServer server = McpServer.async(provider)
                .serverInfo("MCP Server of test", "v0.1")
                .capabilities(capabilities)
                .build();
```

It is important that we also store the `provider` instance, since we cannot access this object through the server after building.
The provider is important to later delegate requests to the server.


### How does Spring handle the automatic registration of Controller and the automatic configured MCP server?

It looks like we have to do the routing ourselves and call the MCP servers manually?
https://stackoverflow.com/questions/40587433/how-to-create-register-and-map-servlets-dynamically-at-runtime
Could also be a possible solution, then we would need to configure Tomcat itself.
https://stackoverflow.com/questions/1884310/dynamically-add-a-servlet-to-the-servletconfig?rq=3

After some more research I think I know what we need to achieve

The MCP Server encapsulates a `WebMvcSseServerTransportProvider` which encapsulates a `RouterFunction` . To enable Spring to automatically route the request to our server we need to register this router function on runtime.

The following bean inside the `McpWebMvcServerAutoConfiguration` is responsible to register the server to spring. Because the bean is inside an AutoConfiguration file the RouterFunction. is automatically registered on startup.

org.springframework.ai.mcp.server.autoconfigure.McpWebMvcServerAutoConfiguration:80
```java
	@Bean
	public RouterFunction<ServerResponse> mvcMcpRouterFunction(WebMvcSseServerTransportProvider transportProvider) {
		return transportProvider.getRouterFunction();
	}
```

The same way we can also register an RouterFunction on startup listening for the two MCP endpoints.
The handler calls a custom handler to forward the request to the correct MCP server

```java
@Configuration
public class McpRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> mcpRoutes(McpHandler handler) {
        return RouterFunctions.route()
                .GET("/sse", handler::handleRequest)
                .POST("/mcp/message", handler::handleRequest)
                .build();
    }
}
```

The current handler implementation always uses the test MCP server.
Here we could implement the selection process for the correct MCP server.

```java
@Component
public class McpHandler {

    private static final Logger logger = LoggerFactory.getLogger(McpHandler.class);

    private final McpServerService mcpServerService;

    public McpHandler(McpServerService mcpServerService) {
        this.mcpServerService = mcpServerService;
    }

    public ServerResponse handleRequest(ServerRequest request) throws Exception {
        String path = request.path();
        String auth = request.headers().firstHeader("Authorization");
        logger.debug("McpHandler auth={}", auth);
        logger.debug("McpHandler path={}", path);

        // instead of get("test") get with username from the token
        Optional<HandlerFunction<ServerResponse>> handlerFunctionOptional = mcpServerService.getProviders().get("test").getRouterFunction().route(request);
        if (handlerFunctionOptional.isPresent()) {
            return handlerFunctionOptional.get().handle(request);
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

**References:**
[^1]: Spring Documentation: https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html
[^2]: Spring Blog on securing a spring mcp server: https://spring.io/blog/2025/05/19/spring-ai-mcp-client-oauth2
[^3]: [RFC 6749: The OAuth 2.0 Authorization Framework](https://datatracker.ietf.org/doc/html/rfc6749)
[^4]: OAuth Basics: https://auth0.com/intro-to-iam/what-is-oauth-2
[^5]: [RFC 6750: The OAuth 2.0 Authorization Framework: Bearer Token Usage](https://datatracker.ietf.org/doc/html/rfc6750#section-3)
[^6]: Spring Documentation: [OAuth 2.0 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html#_specifying_the_authorization_server)
[^7]: [RFC 9728: OAuth 2.0 Protected Resource Metadata](https://datatracker.ietf.org/doc/html/rfc9728#section-3.1)
[^8]: [Stackoverflow How Spring Security filter chain works](https://stackoverflow.com/questions/41480102/how-spring-security-filter-chain-works)
[^9]: [ModelContextProtocol Specification Authorization](https://modelcontextprotocol.io/specification/2025-06-18/basic/authorization)