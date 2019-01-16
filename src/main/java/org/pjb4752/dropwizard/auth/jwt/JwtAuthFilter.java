package org.pjb4752.dropwizard.auth.jwt;

import io.dropwizard.auth.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.crypto.SecretKey;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.security.Principal;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter<P extends Principal> extends AuthFilter<Claims, P> {

    private static Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    public static String JWT_AUTH = "JWT_AUTH";

    private SecretKey signingKey;

    private JwtAuthFilter(Authenticator<Claims, P> authenticator,
                          Authorizer<P> authorizer,
                          UnauthorizedHandler unauthorizedHandler,
                          String prefix,
                          String realm,
                          SecretKey signingKey) {
        this.authenticator = authenticator;
        this.authorizer = authorizer;
        this.unauthorizedHandler = unauthorizedHandler;
        this.prefix = prefix;
        this.realm = realm;
        this.signingKey = signingKey;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        final Claims claims = getJwtClaims(requestContext.getHeaders().
                getFirst(HttpHeaders.AUTHORIZATION));

        if (!authenticate(requestContext, claims, JWT_AUTH)) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }
    }

    private Claims getJwtClaims(String header) {
        if (header == null) {
            return null;
        }
        final String trimmedHeader = header.trim();

        final int space = trimmedHeader.indexOf(' ');
        if (space <= 0) {
            return null;
        }

        final String method = trimmedHeader.substring(0, space);
        if (!prefix.equalsIgnoreCase(method)) {
            return null;
        }

        try {
            final String token = header.substring(space + 1);

            return Jwts.parser().setSigningKey(signingKey).
                    parseClaimsJws(token).getBody();
        } catch (IllegalArgumentException iae) {
            logger.warn("Error decoding credentials", iae);
            return null;
        } catch (JwtException je) {
            logger.warn("Error decoding token", je);
            return null;
        }
    }

    public static class Builder<P extends Principal> {

        private String realm = "realm";
        private String prefix = "Bearer";
        private SecretKey signingKey;
        private Authenticator<Claims, P> authenticator = (credentials) -> Optional.empty();
        private Authorizer<P> authorizer = new PermitAllAuthorizer();
        private UnauthorizedHandler unauthorizedHandler = new DefaultUnauthorizedHandler();

        public Builder<P> setRealm(String realm) {
            this.realm = realm;
            return this;
        }

        public Builder<P> setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder<P> setSigningKey(SecretKey signingKey) {
            this.signingKey = signingKey;
            return this;
        }

        public Builder<P> setAuthorizer(Authorizer<P> authorizer) {
            this.authorizer = authorizer;
            return this;
        }

        public Builder<P> setAuthenticator(Authenticator<Claims, P> authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        public Builder<P> setUnauthorizedHandler(UnauthorizedHandler unauthorizedHandler) {
            this.unauthorizedHandler = unauthorizedHandler;
            return this;
        }

        public JwtAuthFilter<P> buildAuthFilter() {
            requireNonNull(this.realm, "Realm is not set");
            requireNonNull(this.prefix, "Prefix is not set");
            requireNonNull(this.signingKey, "Signing Key is not set");
            requireNonNull(this.authenticator, "Authenticator is not set");
            requireNonNull(this.authorizer, "Authorizer is not set");
            requireNonNull(this.unauthorizedHandler, "Unauthorized handler is not set");

            return new JwtAuthFilter<P>(authenticator, authorizer,
                    unauthorizedHandler, prefix, realm, signingKey);
        }
    }
}

