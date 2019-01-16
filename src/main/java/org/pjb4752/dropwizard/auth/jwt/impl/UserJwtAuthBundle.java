package org.pjb4752.dropwizard.auth.jwt.impl;

import io.dropwizard.Configuration;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.jsonwebtoken.Claims;
import org.pjb4752.dropwizard.auth.jwt.JwtAuthBundle;
import org.pjb4752.dropwizard.auth.jwt.RoleBasedAuthorizer;

public abstract class UserJwtAuthBundle<T extends Configuration>
        extends JwtAuthBundle<T, User> {

    @Override
    public Authenticator<Claims, User> getAuthenticator() {
        return new UserAuthenticator();
    }

    @Override
    public Authorizer<User> getAuthorizer() {
        return new RoleBasedAuthorizer<User>() {};
    }

    @Override
    public Class<User> getPrincipalClass() {
        return User.class;
    }
}
