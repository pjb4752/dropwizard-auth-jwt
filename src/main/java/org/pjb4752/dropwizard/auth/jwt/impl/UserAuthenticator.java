package org.pjb4752.dropwizard.auth.jwt.impl;

import org.pjb4752.dropwizard.auth.jwt.JwtAuthenticator;
import org.pjb4752.dropwizard.auth.jwt.impl.User;

public class UserAuthenticator extends JwtAuthenticator<User> {

    public UserAuthenticator() {
        super(User::new);
    }
}
