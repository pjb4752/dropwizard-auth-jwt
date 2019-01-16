package org.pjb4752.dropwizard.auth.jwt;

import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.Claims;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class JwtAuthenticator<P extends RoleUser> implements Authenticator<Claims, P> {

    private BiFunction<String, List<String>, P> builder;

    public JwtAuthenticator(BiFunction<String, List<String>, P> builder) {
        this.builder = builder;
    }

    @Override
    public Optional<P> authenticate(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        roles.removeAll(Collections.singleton(null));

        return Optional.of(builder.apply(claims.getSubject(), roles));
    }
}
