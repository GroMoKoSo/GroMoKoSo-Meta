package de.thm.mcptest.configuration;

import de.thm.mcptest.security.AccessTokenExtractor;
import de.thm.mcptest.security.OAuthTokenHolder;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
                    OAuthTokenHolder.set(token.getTokenValue());
                    runnable.run();
                } finally {
                    logger.info("[{}] [Reactor Hook] McpUserHolder clean",
                            Thread.currentThread().getName());
                    OAuthTokenHolder.clear();
                }
            };
        };

        Schedulers.onScheduleHook("McpBoundedElasticHook", decorator);
    }
}


