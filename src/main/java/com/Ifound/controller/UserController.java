package com.Ifound.controller;

import com.Ifound.dto.UserDto;
import com.Ifound.exception.UserException;
import com.Ifound.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PutMapping
    public ResponseEntity update(@RequestBody UserDto dto) {
        ResponseEntity responseEntity;
        try {
            responseEntity = ResponseEntity.ok(service.update(dto));
        } catch (UserException e) {
            responseEntity = ResponseEntity.badRequest().body(e.getMessage());
        }
        return responseEntity;
    }

    @PostMapping("/changeBlock")
    public ResponseEntity changeBlock(@RequestBody UserDto dto) {
        ResponseEntity responseEntity ;
        try {
            responseEntity = ResponseEntity.ok(service.activeToggle(dto));
        } catch (UserException e) {
            responseEntity = ResponseEntity.badRequest().body(e.getMessage());
        }
        return responseEntity;
    }

}
