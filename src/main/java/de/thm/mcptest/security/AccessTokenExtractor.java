package de.thm.mcptest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public record AccessTokenExtractor(SecurityContext securityContext) {

    public Jwt extractAccessToken() {
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("Authentication is not a JwtAuthenticationToken");
        }
        return jwtAuth.getToken();
    }
}
