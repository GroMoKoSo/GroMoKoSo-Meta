package de.thm.mcptest;

import de.thm.mcptest.tool.MathTools;
import de.thm.mcptest.service.WeatherService;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@RestController
public class ServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @GetMapping("/updateTools")
    public String greeting() {
        latch.countDown();
        return "Update signal received!";
    }

    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        logger.info("Loading weather tools");
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }

    //@Bean
    public CommandLineRunner commandRunner(McpSyncServer mcpSyncServer) {

        return args -> {

            logger.info("Server: " + mcpSyncServer.getServerInfo());

            SecurityContext context = SecurityContextHolder.getContext();
            if (context.getAuthentication() == null || Objects.equals(context.getAuthentication().getName(), "max")) {
                logger.info("User has no rights to access math tool");
                return;
            }

            List<SyncToolSpecification> newTools = McpToolUtils
                    .toSyncToolSpecifications(ToolCallbacks.from(new MathTools()));

            for (SyncToolSpecification newTool : newTools) {
                logger.info("Add new tool: " + newTool);
                mcpSyncServer.addTool(newTool);
            }

            logger.info("Tools updated: ");
        };
    }
}