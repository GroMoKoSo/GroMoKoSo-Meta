package com.example.mcpdemo;

import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


public class GenericToolCallback implements ToolCallback {

    private final ApiToolSpec spec;
    private final WebClient webClient;

    public GenericToolCallback(ApiToolSpec spec) {
        System.out.println("creating generic ToolCalback for following spec:");
        System.out.println(spec);
        this.spec = spec;
        // you can tune baseUrl, codecs, timeouts, etc. here if you like
        this.webClient = WebClient.builder()
                .baseUrl(spec.getBaseUrl())
                .build();
    }

    @Override
    public ToolDefinition getToolDefinition() {
        // We declare the tool name, description and that it expects a raw string input.
        // If you want richer schemas, you can generate a full JSON Schema here.
        return ToolDefinition.builder()
                .name(spec.getName())
                .description(spec.getDescription())
                .inputSchema("{\"type\":\"string\"}")
                .build();
    }

    @Override
    public String call(String toolInput) {
        System.out.println("calling api ... ");
        HttpMethod method = HttpMethod.valueOf(spec.getMethod().name());

        Mono<String> responseMono;
        if (method == HttpMethod.GET) {
            responseMono = webClient
                    .method(method)
                    .uri(spec.getEndpoint())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class);
        } else {
            responseMono = webClient
                    .method(method)
                    .uri(spec.getEndpoint())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(toolInput)
                    .retrieve()
                    .bodyToMono(String.class);
        }

        System.out.println("fetching ...");
        String result = responseMono.block();
        System.out.println("results:");
        System.out.println(result);
        // block() here to return synchronously; in a real app consider timeouts
        return result;
    }

    @Override
    public String call(String toolInput, ToolContext context) {
        // if you ever want to consume context, remove the guard in the default impl
        return call(toolInput);
    }
}
