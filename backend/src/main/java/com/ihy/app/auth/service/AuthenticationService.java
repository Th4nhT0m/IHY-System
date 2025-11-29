package com.ihy.app.auth.service;

import com.ihy.app.common.constant.ErrorCode;
import com.ihy.app.auth.dto.request.AuthenticationRequest;
import com.ihy.app.auth.dto.request.IntrospectRequest;
import com.ihy.app.auth.dto.response.AuthenticationResponse;
import com.ihy.app.auth.dto.response.IntrospectResponse;
import com.ihy.app.common.exception.AppException;
import com.ihy.app.entity.Users;
import com.ihy.app.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository repository;

    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiratityTimeDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);


        return IntrospectResponse.builder()
                .valid(verified && expiratityTimeDate.after(new Date()))
                .build();
    }


    /**
     * Authenticates a user based on the provided login request
     *
     * @param request  the authentication request
     * @return AuthenticationResponse containing a generated JWT token if authentication succeeds
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Find user in the database by email.
        Users user = repository.findUsersByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        // Check if the user account is active.
        if (user.getIsActive() != 1) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        // Validate the password.
        // `matches()` checks the raw password from the request against the hashed password stored in DB.
        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());

        // If password does not match → throw authentication error
        if (!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATE);
        }

        // Generate a JWT token for the authenticated user.
        var token = generateToken(user);

        // Return the authentication response containing the generated token.
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    /**
     * Generates a signed JWT token for the authenticated user using Nimbus JOSE + JWT library.
     *
     * @param request the authentication request containing user credentials (email, etc.)
     * @return a compact serialized JWS (JWT) string in format: header.payload.signature
     */
    private String generateToken(Users request) {

        //Define the JWT header - specifies signing algorithm (HMAC-SHA256)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        //Build the claims (payload) of the JWT
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(request.getEmail())
                .issueTime(new Date())

                // Token expiration: current time + 1 hour
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))

                .claim("role",buildScope(request))
                .build();
        //Convert claims to JSON payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Create the unsigned JWS object (header + payload)
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // Sign the token
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Token creation failed", e);
            throw new RuntimeException(e);
        }

    }

    private String buildScope(Users users){
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(users.getRoles())){
            users.getRoles().forEach(
                    role -> {
                        joiner.add("ROLE_"+  role.getName());
                        if(!CollectionUtils.isEmpty(role.getPermission())){
                            role.getPermission().forEach(
                                    permission -> {
                                        joiner.add(permission.getName());
                                    }
                            );
                        }

                    }
            );
        }
        return joiner.toString();
    }

}

