package de.thm.mcptest;

import de.thm.mcptest.security.McpUserHolder;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.util.json.schema.JsonSchemaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

//@Service
public class ToolService {

    private static final Logger logger = LoggerFactory.getLogger(ToolService.class);

    private final McpSyncServer server;

    public ToolService(McpSyncServer server) {
        this.server = server;

        // Registering a tool callback programmatically
        Method method = ReflectionUtils.findMethod(DateTimeTools.class,
                "getCurrentDateTime",
                String.class);
        assert method != null;
        logger.info("Json schema for method input: {}",
                JsonSchemaGenerator.generateForMethodInput(method));
        registerTool(method,
                new DateTimeTools(),
                "getCurrentDateTime",
                "Get the current date and time in ISO format",
                JsonSchemaGenerator.generateForMethodInput(method));
        String inputSchema = """
                {
                    "type": "object",
                    "properties": {
                        "postId": {
                            "type": "integer",
                            "description": "The ID of post to retrieve"
                        }
                    },
                    "required": ["postId"],
                    "additionalProperties": false
                }
                """;
//        registerTool(ReflectionUtils.findMethod(ToolService.class, "invokeTool", Object[].class), null, "getPosts",
//                "Get a detailed information about a single post using its id", inputSchema);

        test("getPosts",
                "Get a detailed information about a single post using its id",
                inputSchema,
                ToolService::invokeTool);

        logger.info(server.getServerCapabilities().toString());
        logger.info(server.getServerInfo().toString());
    }

    public static Object invokeTool(Object... args) {
        logger.debug("Invoking Tool...");
        for (Object arg : args) {
            logger.info("arg: {}", arg);
        }
        logger.info("passed args: {}", args);
        assert args.length == 1 : "Expected exactly one argument, got: " + args.length;
        RestClient restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
        String result = restClient.get()
                .uri("/posts/{postId}", args[0])
                .retrieve()
                .body(String.class);
        logger.info("Result: {}", result);
        return result;
    }

    public void registerTool(Method toolMethod, Object toolObject, String toolName,
                             String toolDescription, String inputSchema) {
        ToolDefinition toolDefinition = ToolDefinition.builder()
                .name(toolName)
                .description(toolDescription)
                .inputSchema(inputSchema)
                .build();

        MethodToolCallback.Builder builder = MethodToolCallback.builder()
                .toolDefinition(toolDefinition)
                .toolMethod(toolMethod);
        if (toolObject != null) {
            builder.toolObject(toolObject);
        }
        ToolCallback toolCallback = builder.build();

        server.addTool(McpToolUtils.toSyncToolSpecification(toolCallback));
        logger.info("Registered method tool: {}", toolName);
    }

    public void registerTool(Supplier<?> toolMethod, String toolName, String toolDescription) {
        ToolCallback toolCallback = FunctionToolCallback
                .builder(toolName, toolMethod)
                .description(toolDescription)
                .build();

        server.addTool(McpToolUtils.toSyncToolSpecification(toolCallback));
        logger.info("Registered function tool: {}", toolName);

    }

    public void test(String name, String description, String inputSchema,
                     Function<Object[], Object> toolFunction) {
        McpServerFeatures.SyncToolSpecification tool = new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(
                        name,
                        description,
                        inputSchema
                ),
                (exchange, args) -> {
                    try {
                        logger.info("Executing Tool with user " + McpUserHolder.get());
                        // TODO: How to handle the args dynamically?
                        Object expr = args.get("postId");
                        String callResult = toolFunction.apply(new Object[]{expr}).toString();
                        return new McpSchema.CallToolResult(
                                List.of(new McpSchema.TextContent(callResult)), false);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        return new McpSchema.CallToolResult(
                                List.of(new McpSchema.TextContent(e.getMessage())), true);
                    } finally {
                        McpUserHolder.clear();
                    }
                }
        );
        server.addTool(tool);
    }
}
