package de.thm.mcptest.service;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    private final RestClient restClient;

    public WeatherService() {
        this.restClient = RestClient.builder()
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
                .build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WeatherResponse(@JsonProperty("current") Current current) { // @formatter:off
        public record Current(
                @JsonProperty("time") LocalDateTime time,
        @JsonProperty("interval") int interval,
        @JsonProperty("temperature_2m") double temperature_2m) {
        }
    } // @formatter:on

    @Tool(description = "Get the temperature (in celsius) for a specific location") // @formatter:off
    public WeatherResponse weatherForecast(
            @ToolParam(description = "The location latitude") double latitude,
            @ToolParam(description = "The location longitude") double longitude,
            ToolContext toolContext) { // @formatter:on

        WeatherResponse weatherResponse = restClient
                .get()
                .uri("https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m",
                        latitude, longitude)
                .retrieve()
                .body(WeatherResponse.class);

        return weatherResponse;
    }
}