package org.pjb4752.dropwizard.auth.jwt.login;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private LoginRequest() {
        // Jackson Serialization
    }
}

