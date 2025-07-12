package com.example.mcpdemo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiToolSpec {
    private String name;
    private String description;
    private String baseUrl;
    private String endpoint;
    private HttpMethod method;
}
