package org.pjb4752.dropwizard.auth.jwt.login;

import com.codahale.metrics.annotation.Timed;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.pjb4752.dropwizard.auth.jwt.RoledPrincipal;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource<P extends RoledPrincipal> {

    private SecretKey signingKey;

    private LoginService<P> loginService;

    private Optional<Long> loginDuration;

    public LoginResource(SecretKey signingKey, LoginService<P> loginService) {
        this(signingKey, loginService, Optional.empty());
    }

    public LoginResource(SecretKey signingKey, LoginService<P> loginService,
                         Optional<Long> loginDuration) {
        this.signingKey = signingKey;
        this.loginService = loginService;
        this.loginDuration = loginDuration;
    }

    @POST
    @Timed
    public LoginResponse create(@NotNull @Valid LoginRequest request) {
        final String token = loginService.login(request).map(this::createToken).
                orElseThrow(this::failedLoginException);

        return new LoginResponse(token);
    }

    private String createToken(P roleUser) {
        final JwtBuilder builder = Jwts.builder().
                setSubject(roleUser.getName()).
                claim("roles", roleUser.getRoles());

        return setExpiration(builder).signWith(signingKey).compact();
    }

    private WebApplicationException failedLoginException() {
       return new WebApplicationException("Invalid credentials", Response.Status.BAD_REQUEST);
    }

    private JwtBuilder setExpiration(JwtBuilder builder) {
        return loginDuration.map(seconds -> {
            final Instant expiration = Instant.now().plusSeconds(seconds);
            return builder.setExpiration(Date.from(expiration));
        }).orElse(builder);
    }
}
