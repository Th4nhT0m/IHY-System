package com.ihy.app.auth.service;

import com.ihy.app.auth.dto.request.AuthenticationRequest;
import com.ihy.app.auth.dto.request.LogoutRequest;
import com.ihy.app.auth.dto.request.RefreshRequest;
import com.ihy.app.auth.dto.response.AuthenticationResponse;
import com.ihy.app.auth.entity.InvalidateToken;
import com.ihy.app.auth.repository.InvalidateTokenRepository;
import com.ihy.app.common.constant.ErrorCode;
import com.ihy.app.common.exception.AppException;
import com.ihy.app.entity.Users;
import com.ihy.app.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    InvalidateTokenRepository invalidateTokenRepository;

    IntrospectService introspectService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    @NonFinal
    @Value("${jwt.expiration}")
    protected long EXPIRATION_TIME;

    /**
     * Authenticates a user based on the provided login request
     *
     * @param request the authentication request
     * @return AuthenticationResponse containing a generated JWT token if authentication succeeds
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Find user in the database by email.
        Users user = userRepository.findUsersByEmail(request.getEmail())
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
                        Instant.now().plus(EXPIRATION_TIME, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("role", buildScope(request))
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

    /**
     * Refreshes the JWT token using a valid refresh token
     *
     * @param request - Object containing the refresh token
     * @return AuthenticationResponse - New authentication token
     * @throws ParseException - Token parsing exception
     * @throws JOSEException - Exception related to JWT processing
     */
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {

        // Verify the refresh token and parse it into SignedJWT object
        var signToken = introspectService.verifyToken(request.getToken(), true);

        // Extract JWT ID and expiration time from the token
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        // Create invalidate token entry to blacklist the old refresh token
        // This prevents the same refresh token from being reused
        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jit)
                .expiryDate(expiryTime)
                .build();
        invalidateTokenRepository.save(invalidateToken);

        // Extract user email from token subject
        String userEmail = signToken.getJWTClaimsSet().getSubject();

        // Find user by email
        Users user = userRepository.findUsersByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATE));

        // Generate new token pair for the user
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }


    /**
     * Logs out user by invalidating their token
     *
     * @param request - Object containing the token to be invalidated
     * @throws ParseException - Token parsing exception
     * @throws JOSEException - Exception related to JWT processing
     */
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            // Verify the token before invalidating it
            var signToken = introspectService.verifyToken(request.getToken(), true);

            // Extract JWT ID and expiration time from the token
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            // Create invalidate token entry to blacklist the token
            // This prevents the token from being used again
            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .id(jit)
                    .expiryDate(expiryTime)
                    .build();
            invalidateTokenRepository.save(invalidateToken);

        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    /**
     * Builds scope string containing roles and permissions for JWT token
     *
     * @param users - User object containing roles and permissions
     * @return String - Space-separated scope string (e.g., "ROLE_ADMIN READ_DATA WRITE_DATA")
     */
    private String buildScope(Users users) {
        // StringJoiner to concatenate scopes with space delimiter
        StringJoiner joiner = new StringJoiner(" ");

        // Check if user has any roles
        if (!CollectionUtils.isEmpty(users.getRoles())) {
            users.getRoles().forEach(
                    role -> {
                        // Add role to scope with "ROLE_" prefix
                        joiner.add("ROLE_" + role.getName());

                        // Check if role has any permissions
                        if (!CollectionUtils.isEmpty(role.getPermission())) {
                            // Iterate through each permission of the role
                            role.getPermission().forEach(
                                    permission -> {
                                        // Add permission to scope
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

