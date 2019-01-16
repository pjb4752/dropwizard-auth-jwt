package org.pjb4752.dropwizard.auth.jwt;

import io.dropwizard.auth.Authorizer;

public class RoleBasedAuthorizer<P extends RoleUser> implements Authorizer<P> {

    @Override
    public boolean authorize(P principal, String role) {
        return principal.hasRole(role);
    }
}
