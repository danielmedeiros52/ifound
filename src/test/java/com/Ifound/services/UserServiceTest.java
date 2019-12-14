package com.Ifound.services;

import com.Ifound.dto.UserDto;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.exception.UserException;
import com.Ifound.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    static UserDto userDto;
    static String credential;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

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
        assertTrue(authService.verifyUserToRegister(userDto));
        userService.register(userDto, credential);
        assertThrows(IFoundAuthenticationException.class, () -> authService.verifyUserToRegister(userDto));
    }

    @Test
    void update() throws UserAuthenticationException, IFoundAuthenticationException, UserException {
        userService.register(userDto, credential);
        User user = userService.loadUserBynumberOfPrincipalDocument(userDto.getNumberOfPrincipalDocument()).get();
        assertEquals(userDto.getName(), user.getName());
        user.setName("User Unit Test");
        assertNotEquals(userDto.getName(), user.getName());
        userService.update(user);
        User userUnitTest = userService.loadUserBynumberOfPrincipalDocument(userDto.getNumberOfPrincipalDocument()).get();

    }

    @Test
    void loadUserByNumberOfPrincipalDocument() {
        User user = userService.loadUserBynumberOfPrincipalDocument(userDto.getNumberOfPrincipalDocument()).get();
        assertEquals(userDto.getCellphone(), user.getCellphone());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getCity(), user.getCity());
        assertEquals(userDto.getNumberOfPrincipalDocument(), user.getNumberOfPrincipalDocument());
        assertEquals(userDto.getStreet(), user.getStreet());
        assertEquals(userDto.getHomeNumber(), user.getHomeNumber());
        assertEquals(userDto.getZipCode(), user.getZipCode());

    }

    @Test
    void activeToggle() {
    }

    @Test
    void authenticate() {
    }
}