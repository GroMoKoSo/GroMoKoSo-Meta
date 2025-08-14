package de.thm.mcptest.security;

import de.thm.mcptest.ToolService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;


@Configuration
public class ReactorElasticBoundConfig {

    private static final Logger logger = LoggerFactory.getLogger(ReactorElasticBoundConfig.class);

    @PostConstruct
    public void init() {
        Function<Runnable, Runnable> decorator = runnable -> {
            Jwt token = new AccessTokenExtractor(SecurityContextHolder.getContext())
                    .extractAccessToken();

            return () -> {
                try {
                    McpUserHolder.set(token.getTokenValue());
                    runnable.run();
                } finally {
                    logger.info("[{}] [Reactor Hook] McpUserHolder clean",
                            Thread.currentThread().getName());
                    McpUserHolder.clear();
                }
            };
        };

        Schedulers.onScheduleHook("McpBoundedElasticHook", decorator);
    }
}


record AccessTokenExtractor(SecurityContext securityContext) {

    public Jwt extractAccessToken() {
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("Authentication is not a JwtAuthenticationToken");
        }
        return jwtAuth.getToken();
    }
}