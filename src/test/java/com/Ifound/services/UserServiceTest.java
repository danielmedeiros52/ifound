package com.Ifound.services;

import com.Ifound.dto.UserDto;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.exception.UserException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserServiceTest {
    static UserDto userDto;
    static String credential;
    @Autowired
    UserService userService;

    @BeforeAll
    static void setProperties() {
        userDto = UserDto.builder()
                .email("teste@teste.pt")
                .name("User test")
                .cellphone("99999999")
                .numberOfPrincipalDocument("00000000000")
                .city("Lisbon")
                .street("Test Avenue ")
                .type("found")
                .homeNumber("2")
                .zipCode("2830-085")
                .build();

        credential = "bG9naW46cGFzc3dvcmQ=";
    }

    @Test
    @Transactional
    void register() throws UserAuthenticationException, IFoundAuthenticationException, UserException {
        userService.register(userDto, credential);
    }

    @Test
    void update() {
    }

    @Test
    void activeToggle() {
    }

    @Test
    void authenticate() {
    }
}