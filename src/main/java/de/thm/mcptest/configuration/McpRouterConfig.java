package de.thm.mcptest.configuration;

import de.thm.mcptest.controller.McpRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class McpRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> mcpRoutes(McpRequestHandler handler) {
        return RouterFunctions.route()
                .GET("/sse", handler::handleRequest)
                .POST("/mcp/message", handler::handleRequest)
                .build();
    }
}