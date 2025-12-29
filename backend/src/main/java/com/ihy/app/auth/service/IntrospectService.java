package com.ihy.app.auth.service;

import com.ihy.app.auth.dto.request.IntrospectRequest;
import com.ihy.app.auth.dto.response.IntrospectResponse;
import com.ihy.app.auth.repository.InvalidateTokenRepository;
import com.ihy.app.common.constant.ErrorCode;
import com.ihy.app.common.exception.AppException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IntrospectService {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    @NonFinal
    @Value("${jwt.expiration}")
    protected long EXPIRATION_TIME;

    @NonFinal
    @Value("${jwt.refreshable}")
    protected long REFRESHABLE_TIME;


    InvalidateTokenRepository invalidateTokenRepository;


    /**
     * Validates the JWT token
     *
     * @param request - Object containing the token to be validated
     * @return IntrospectResponse - Token validation result (valid/invalid)
     * @throws JOSEException - Exception related to JWT processing
     * @throws ParseException - Token parsing exception
     */
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {

        boolean valid = true;
        var token = request.getToken();

        try {
            // Call token verification method (not a refresh token)
            verifyToken(token,false);

        } catch (AppException e) {
            valid = false;
        }

        return IntrospectResponse.builder()
                .isValid(valid)
                .build();
    }

    /**
     * Verifies JWT token and checks validity conditions
     *
     * @param token - JWT token string to be verified
     * @param isRefresh - Flag to determine token type (true: refresh token, false: access token)
     * @return SignedJWT - Verified JWT object
     * @throws JOSEException - Exception related to JWT processing
     * @throws ParseException - Token parsing exception
     */
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // Determine expiration time based on token type
        Date expiratityTimeDate = isRefresh
                                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_TIME, ChronoUnit.HOURS).toEpochMilli())   //refresh token
                                : signedJWT.getJWTClaimsSet().getExpirationTime();   //authenticate, logout

        // Verify token signature
        var verified = signedJWT.verify(verifier);

        // Check valid signature and token not expired
        if(!(verified && expiratityTimeDate.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATE);
        }

        //Check token has been invalidated
        if(invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
        {
            throw new AppException(ErrorCode.UNAUTHENTICATE);
        }

        // Token is valid, return SignedJWT object
        return signedJWT;
    }
}
