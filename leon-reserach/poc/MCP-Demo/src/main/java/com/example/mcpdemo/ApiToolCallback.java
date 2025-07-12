package com.example.mcpdemo;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;

public class ApiToolCallback implements ToolCallback {
    @Override
    public ToolDefinition getToolDefinition() {
        return null;
    }

    @Override
    public String call(String toolInput) {
        return "";
    }
}
