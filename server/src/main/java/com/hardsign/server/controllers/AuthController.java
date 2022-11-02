package com.hardsign.server.controllers;

import com.hardsign.server.models.auth.JwtRequest;
import com.hardsign.server.models.auth.JwtResponse;
import com.hardsign.server.models.auth.RefreshJwtRequest;
import com.hardsign.server.services.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        try{
            var token = authService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity
                    .status(401)
                    .build();
        }
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        try{
            var token = authService.getAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity
                    .status(401)
                    .build();
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        try{
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity
                    .status(401)
                    .build();
        }
    }
}
