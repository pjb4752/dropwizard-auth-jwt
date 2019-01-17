package org.pjb4752.dropwizard.auth.jwt.login;

public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private LoginResponse() {
        // Jackson serialization
    }
}
