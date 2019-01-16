package org.pjb4752.dropwizard.auth.jwt.login;

import org.pjb4752.dropwizard.auth.jwt.RoledPrincipal;

import java.util.Optional;

public interface LoginService<P extends RoledPrincipal> {

    Optional<P> login(LoginRequest request);
}
