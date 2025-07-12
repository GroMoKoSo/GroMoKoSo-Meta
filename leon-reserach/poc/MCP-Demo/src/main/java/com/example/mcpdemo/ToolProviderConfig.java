package com.example.mcpdemo;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

import java.util.List;

public class ToolProviderConfig {
    public ToolCallbackProvider dynamicToolProvider;

    public ToolProviderConfig() {
        ToolFactory factory = new DefaultToolFactory();
        List<ToolCallback> callBacks = factory.createTools(loadSpecs());
        dynamicToolProvider = ToolCallbackProvider.from(callBacks);
    }

    private List<ApiToolSpec> loadSpecs() {
        return List.of(
                new ApiToolSpec("weather", "gets the weather forecast for the city wetzlar", "https://api.open-meteo.com", "/v1/forecast?latitude=50.5611&longitude=8.5049&hourly=temperature_2m", HttpMethod.GET),
                new ApiToolSpec("random facts", "get random facts", "https://uselessfacts.jsph.pl","/api/v2/facts/random", HttpMethod.GET)
        );
    }
}
