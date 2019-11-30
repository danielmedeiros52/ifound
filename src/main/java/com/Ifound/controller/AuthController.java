package com.Ifound.controller;

import com.Ifound.dto.UserDto;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody UserDto dto) {
        ResponseEntity responseEntity = null;
        try {
            responseEntity = ResponseEntity.ok(userService.register(dto));
        } catch (IFoundAuthenticationException e) {
            responseEntity = ResponseEntity.badRequest().body(e.getMessage());
        }
        return responseEntity;
    }
}
