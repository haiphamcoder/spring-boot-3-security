package com.haiphamcoder.demo.adapter.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haiphamcoder.demo.application.service.AuthenticationService;
import com.haiphamcoder.demo.domain.model.AuthenticationRequest;
import com.haiphamcoder.demo.domain.model.AuthenticationResponse;
import com.haiphamcoder.demo.domain.model.RegisterRequest;
import com.haiphamcoder.demo.shared.ApiResponse;
import com.haiphamcoder.demo.shared.ApiResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController extends BaseController{
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok().body(ApiResponseFactory.createSuccessResponse(response));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok().body(ApiResponseFactory.createSuccessResponse(response));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
