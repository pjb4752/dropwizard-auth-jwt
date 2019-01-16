package org.pjb4752.dropwizard.auth.jwt;

import io.dropwizard.Configuration;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.jsonwebtoken.Claims;

public abstract class RoleUserJwtAuthBundle<T extends Configuration>
        extends JwtAuthBundle<T, RoleUser> {

    @Override
    public Authenticator<Claims, RoleUser> getAuthenticator() {
        return new RoleUserAuthenticator();
    }

    @Override
    public Authorizer<RoleUser> getAuthorizer() {
        return new RoleBasedAuthorizer<RoleUser>();
    }

    @Override
    public Class<RoleUser> getPrincipalClass() {
        return RoleUser.class;
    }
}
