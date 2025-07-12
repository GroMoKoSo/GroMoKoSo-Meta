package com.example.mcpdemo;

import java.util.List;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class, args);
    }

    // This bean exposes our ShoppingCart tools to the MCP framework
    @Bean
    public ToolCallbackProvider registryToolCallbacks() {
        return new ToolProviderConfig().dynamicToolProvider;
    }

    //@Bean
    public List<ToolCallback> shoppingCartToolCallbacks(ShoppingCart shoppingCart) {
        // The ShoppingCart service is auto-injected by Spring
        return List.of(ToolCallbacks.from(shoppingCart));
    }
}