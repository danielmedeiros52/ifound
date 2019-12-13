package com.Ifound.services;

import com.Ifound.dto.UserDto;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.exception.UserAuthenticationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
class AuthServiceTest {
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    //verify if not exist a user with this email or document principal registered.
    @Test
    @ExceptionHandler
    void verifyUserToRegisterIfNotExist() throws IFoundAuthenticationException {
        UserDto userDto = mock(UserDto.class);
        when(userDto.getEmail()).thenReturn("teste@teste.pt");
        when(userDto.getNumberOfPrincipalDocument()).thenReturn("123");
        assertTrue(authService.verifyUserToRegister(userDto));
    }

    @Test
    void extractUserNameAndPassword() throws UserAuthenticationException {
        final String pass = "bG9naW46cGFzc3dvcmQ=";
        final Pair loginPassword;
        loginPassword = authService.extractUserNameAndPassword(pass);
        assertEquals("login", loginPassword.getFirst());
        assertEquals("password", loginPassword.getSecond());

    }

    @Test
    void extractUserNameAndPasswordException() {
        final String pass = "3412lkjd91234f";
        assertThrows(UserAuthenticationException.class, () -> authService.extractUserNameAndPassword(pass));
    }
}