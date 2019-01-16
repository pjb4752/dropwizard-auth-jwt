package org.pjb4752.dropwizard.auth.jwt.impl;

import org.pjb4752.dropwizard.auth.jwt.RoledPrincipal;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class User implements RoledPrincipal {
    final private String name;
    final private List<String> roles;

    public User(String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final RoledPrincipal otherUser = (RoledPrincipal) other;
        return Objects.equals(name, otherUser.getName()) &&
                Objects.equals(roles, otherUser.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, roles);
    }

    @Override
    public String toString() {
        final String roles = getRoles().stream().collect(Collectors.joining(", "));

        return String.format("RoledPrincipal{name='%s', roles='%s'}", name, roles);
    }
}
