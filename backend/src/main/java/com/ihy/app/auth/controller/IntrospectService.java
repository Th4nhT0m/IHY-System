package com.ihy.app.auth.controller;

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
import java.util.Date;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IntrospectService {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    InvalidateTokenRepository invalidateTokenRepository;


    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {

        boolean valid = true;
        var token = request.getToken();

        try {
            verifyToken(token);

        } catch (AppException e) {
            valid = false;
        }

        return IntrospectResponse.builder()
                .isValid(valid)
                .build();
    }


    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiratityTimeDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        if(!(verified && expiratityTimeDate.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATE);
        }

        if(invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
        {
            throw new AppException(ErrorCode.UNAUTHENTICATE);
        }

        return signedJWT;

    }
}
