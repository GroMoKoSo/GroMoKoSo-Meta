package de.thm.mcptest;

import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Objects;

public class GenericRestClient {

    private final String url;
    private final ArrayList<Object> parameters = new ArrayList<>();

    public GenericRestClient(String url, Object ... parameters) {
        this.url = url;
        for (Object parameter : parameters) {
            if (Objects.nonNull(parameter)) {
                this.parameters.add(parameter);
            }
        }
    }

    public String get(Object... parameters) {
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }



}
