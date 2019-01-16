package org.pjb4752.dropwizard.auth.jwt;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.jsonwebtoken.Claims;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.crypto.SecretKey;

public abstract class JwtAuthBundle<T extends Configuration, P extends RoledPrincipal>
        implements ConfiguredBundle<T> {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(T configuration, Environment environment) {
        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<P>().
                        setSigningKey(getSigningKey(configuration)).
                        setAuthenticator(getAuthenticator()).
                        setAuthorizer(getAuthorizer()).
                        buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().
                register(new AuthValueFactoryProvider.Binder<>(getPrincipalClass()));
    }

    public abstract SecretKey getSigningKey(T configuration);

    public abstract Authenticator<Claims, P> getAuthenticator();

    public abstract Authorizer<P> getAuthorizer();

    public abstract Class<P> getPrincipalClass();
}
