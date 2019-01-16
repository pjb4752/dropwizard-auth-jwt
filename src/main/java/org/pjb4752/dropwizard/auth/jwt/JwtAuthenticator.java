package org.pjb4752.dropwizard.auth.jwt;

import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.Claims;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class JwtAuthenticator<P extends RoledPrincipal> implements Authenticator<Claims, P> {

    private BiFunction<String, List<String>, P> builder;

    public JwtAuthenticator(BiFunction<String, List<String>, P> builder) {
        this.builder = builder;
    }

    @Override
    public Optional<P> authenticate(Claims claims) {
        if (isTokenExpired(claims.getExpiration())) {
            return Optional.empty();
        }

        List<String> roles = (List<String>) claims.get("roles");
        roles.removeAll(Collections.singleton(null));

        return Optional.of(builder.apply(claims.getSubject(), roles));
    }

    private boolean isTokenExpired(Date expirationDate) {
        return Optional.ofNullable(expirationDate).
                map(date -> date.toInstant().compareTo(Instant.now()) < 0).
                orElse(false);
    }
}
