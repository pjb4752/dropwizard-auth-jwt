package org.pjb4752.dropwizard.auth.jwt;

import java.security.Principal;
import java.util.List;

public interface RoledPrincipal extends Principal {

    List<String> getRoles();

    default boolean hasRole(String role) {
        return getRoles().contains(role);
    }
}
