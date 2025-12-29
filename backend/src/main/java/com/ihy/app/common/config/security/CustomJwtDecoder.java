package com.ihy.app.common.config.security;

import com.ihy.app.auth.service.IntrospectService;
import com.ihy.app.auth.dto.request.IntrospectRequest;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private IntrospectService introspectService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    /**
     * Custom decode jwt
     * @param token
     * @return
     */
    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            var response = introspectService.introspect(IntrospectRequest.builder().token(token).build());

            if (!response.isValid()) throw new JwtException("Token invalid");

        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }


        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGN_KEY.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }

}
