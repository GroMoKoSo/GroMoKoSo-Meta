package com.example.mcpdemo;



import org.springframework.ai.tool.ToolCallback;

import java.util.List;

public class DefaultToolFactory implements ToolFactory {
    @Override
    public List<ToolCallback> createTools(List<ApiToolSpec> specs) {
        return specs.stream()
                .map(spec -> (ToolCallback) new GenericToolCallback(spec))
                .toList(); // oder .collect(Collectors.toList()) bei Java 8
    }
}
