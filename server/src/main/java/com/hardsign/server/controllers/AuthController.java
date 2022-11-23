package com.hardsign.server.controllers;

import com.hardsign.server.exceptions.BadRequestException;
import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.exceptions.UnauthorizedException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.auth.JwtRequest;
import com.hardsign.server.models.auth.JwtTokens;
import com.hardsign.server.models.auth.JwtTokensModel;
import com.hardsign.server.models.auth.RefreshJwtRequest;
import com.hardsign.server.services.auth.AuthService;
import com.hardsign.server.services.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final Mapper mapper;

    public AuthController(AuthService authService, UserService userService, Mapper mapper) {
        this.authService = authService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("login")
    public JwtTokensModel login(@RequestBody JwtRequest authRequest) {
        var login = authRequest.getLogin();
        var user = userService.findUserByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found."));

        var password = authRequest.getPassword();
        if (!authService.verifyPassword(user, password))
            throw new UnauthorizedException("Wrong login or password.");

        var tokens = authService.login(user);

        return mapper.mapToModel(tokens);
    }

    @PostMapping("token")
    public JwtTokensModel getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        var refreshToken = request.getRefreshToken();
        var claims = authService.getRefreshClaims(refreshToken)
                .orElseThrow(() -> new BadRequestException("Invalid token."));
        var login = claims.getSubject();

        var user = userService.findUserByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (!authService.verifyToken(login, refreshToken))
            throw new BadRequestException("Invalid token.");

        var accessToken = authService.generateAccessToken(user);
        var jwtTokens = new JwtTokens(accessToken, null);

        return mapper.mapToModel(jwtTokens);
    }

    @PostMapping("refresh")
    public JwtTokensModel getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        var refreshToken = request.getRefreshToken();
        var claims = authService.getRefreshClaims(refreshToken)
                .orElseThrow(() -> new BadRequestException("Invalid token."));

        var login = claims.getSubject();
        if (!authService.verifyToken(login, refreshToken))
            throw new BadRequestException("Invalid token.");

        var user = userService.findUserByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found."));
        var tokens = authService.refresh(user);

        return mapper.mapToModel(tokens);
    }

}
