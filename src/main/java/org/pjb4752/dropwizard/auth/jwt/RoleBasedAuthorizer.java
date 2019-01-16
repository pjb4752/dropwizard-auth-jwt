package org.pjb4752.dropwizard.auth.jwt;

import io.dropwizard.auth.Authorizer;
import org.pjb4752.dropwizard.auth.jwt.RoledPrincipal;

public interface RoleBasedAuthorizer<P extends RoledPrincipal> extends Authorizer<P> {

    @Override
    default boolean authorize(P principal, String role) {
        return principal.hasRole(role);
    }
}
