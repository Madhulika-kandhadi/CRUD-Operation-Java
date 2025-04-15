package com.talentreef.interviewquestions.takehome.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "cors")
@Getter
@Setter
@Primary
public class CorsProperties {
    private List<String> allowedOrigins = List.of();
    private List<String> allowedMethods = List.of();
    private List<String> allowedHeaders = List.of();
    private boolean allowCredentials = false;
    private long maxAge = 3600;
}