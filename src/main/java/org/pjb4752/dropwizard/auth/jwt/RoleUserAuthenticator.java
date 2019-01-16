package org.pjb4752.dropwizard.auth.jwt;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RoleUserAuthenticator extends JwtAuthenticator<RoleUser> {

    public RoleUserAuthenticator() {
        super((name, roles) ->
            new RoleUser() {
                String name;
                List<String> roles;

                @Override
                public String getName() {
                    return name;
                }

                @Override
                public List<String> getRoles() {
                    return roles;
                }

                public RoleUser init(String name, List<String> roles) {
                    this.name = name;
                    this.roles = roles;

                    return this;
                }

                @Override
                public boolean equals(Object other) {
                    if (this == other) {
                        return true;
                    }
                    if (other == null || getClass() != other.getClass()) {
                        return false;
                    }

                    final RoleUser otherUser = (RoleUser) other;
                    return Objects.equals(name, otherUser.getName()) &&
                            Objects.equals(roles, otherUser.getRoles());
                }

                @Override
                public int hashCode() {
                    return Objects.hash(name, roles);
                }

                @Override
                public String toString() {
                    final String roles = getRoles().stream().
                            collect(Collectors.joining(", "));

                    return String.format("RoleUser{name='%s', roles='%s'}", name, roles);
                }
            }.init(name, roles));
    }
}
