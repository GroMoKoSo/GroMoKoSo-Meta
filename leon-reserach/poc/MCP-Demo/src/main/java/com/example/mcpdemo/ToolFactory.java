package com.example.mcpdemo;


import org.springframework.ai.tool.ToolCallback;

import java.util.List;

public interface ToolFactory {
    public List<ToolCallback> createTools(List<ApiToolSpec> specs);
}
