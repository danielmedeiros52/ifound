package com.Ifound.controller;

import com.Ifound.dto.UserDto;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.exception.UserException;
import com.Ifound.model.IFoundUserDetails;
import com.Ifound.services.UserDetailService;
import com.Ifound.services.UserService;
import com.Ifound.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final String tokenHeader;
    private  final  UserDetailService userDetailService;

    @Autowired
    public AuthController(UserService userService, JwtUtils jwtUtils, @Value("${jwt.header}") String tokenHeader, UserDetailService userDetailService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.tokenHeader = tokenHeader;
        this.userDetailService = userDetailService;
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody UserDto dto,@RequestHeader(name = "Authorization") String credentials) {
        ResponseEntity responseEntity = null;
        try {
            responseEntity = ResponseEntity.ok(userService.register(dto, credentials));
        } catch (IFoundAuthenticationException | UserException | UserAuthenticationException e) {
            responseEntity = ResponseEntity.badRequest().body(e.getMessage());
        }
        return responseEntity;
    }

    public String userLogin(@RequestHeader("Authorization") String credentials, HttpServletResponse response) {
        try {
            return userService.authenticate(credentials);
        } catch (UserAuthenticationException ex) {
            response.setStatus(HttpStatus.ACCEPTED.value());
            return ex.getMessage();
        }
    }
    @GetMapping("/refresh")
    public ResponseEntity jwtGetRefresh(HttpServletRequest request) throws UserAuthenticationException {
        final String authToken = request.getHeader(tokenHeader);
        String username = jwtUtils.getUsernameFromToken(authToken);
        IFoundUserDetails user = (IFoundUserDetails) userDetailService.loadUserByUsername(username);
        if (jwtUtils.canTokenBeRefreshed(authToken, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtUtils.refreshToken(authToken);
            return ResponseEntity.ok(refreshedToken);
        } else {
            return  ResponseEntity.badRequest().body(404);
        }
    }

    @GetMapping("/sessionActive")
    public void sessionActive( HttpServletResponse response , HttpServletRequest request) throws UserAuthenticationException {
        final String authToken = request.getHeader(tokenHeader);
        if (jwtUtils.validExpiration(authToken)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasAnyAuthority('AUTH_VIEWER','AUTH_USER','AUTH_MANAGER','AUTH_ADMIN')")
    @GetMapping("/register2")
    public String teste(){
        return "pong";
    }

}
