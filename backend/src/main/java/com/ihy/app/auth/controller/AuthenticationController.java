package com.ihy.app.auth.controller;

import com.ihy.app.auth.dto.request.LogoutRequest;
import com.ihy.app.common.constant.AppConstants;
import com.ihy.app.auth.dto.request.AuthenticationRequest;
import com.ihy.app.auth.dto.request.IntrospectRequest;
import com.ihy.app.common.dto.response.ApiResponse;
import com.ihy.app.auth.dto.response.AuthenticationResponse;
import com.ihy.app.auth.dto.response.IntrospectResponse;
import com.ihy.app.auth.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService service;

    IntrospectService introspectService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = service.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(AppConstants.SUCCESS_CODE)
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> Introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = introspectService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .code(AppConstants.SUCCESS_CODE)
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        service.logout(request);
        return ApiResponse.<Void>builder()
                .message(AppConstants.SUCCESS_CODE)
                .build();
    }


}
