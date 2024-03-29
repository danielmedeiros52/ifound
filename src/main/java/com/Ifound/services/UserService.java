package com.Ifound.services;

import com.Ifound.authentication.JwtGenerator;
import com.Ifound.dao.UserDAO;
import com.Ifound.dto.UserDto;
import com.Ifound.enumerates.EnumUserType;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.exception.UserException;
import com.Ifound.model.User;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final AuthService authService;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final UserDAO dao;
    private Clock clock = DefaultClock.INSTANCE;

    @Autowired
    public UserService(AuthService authService, JwtGenerator jwtGenerator, PasswordEncoder passwordEncoder, UserDAO dao) {
        this.authService = authService;
        this.jwtGenerator = jwtGenerator;
        this.passwordEncoder = passwordEncoder;
        this.dao = dao;
    }

    public User register(UserDto dto,String credentials) throws IFoundAuthenticationException, UserException, UserAuthenticationException {
       Pair<String,String> userNameAndPassword = authService.extractUserNameAndPassword(credentials);
        if (dao.existsByUsername(userNameAndPassword.getFirst())) {
            throw new UserAuthenticationException("User already exists!");
        }
        authService.verifyUserToRegister(dto);
        User user = parseDtoToModel(dto);
        user.setUsername(userNameAndPassword.getFirst());
        user.setPassword(passwordEncoder.encode(userNameAndPassword.getSecond()));
        return save(user);
    }

    public User update(UserDto dto) throws UserException {
        try {
            return save(dto);
        } catch (Exception ex) {
            throw new UserException(ex.getMessage());
        }
    }

    public User update(User user) throws UserException {
        try {
            return save(user);
        } catch (Exception ex) {
            throw new UserException(ex.getMessage());
        }
    }

    private User save(UserDto dto) throws UserException {
        return save(parseDtoToModel(dto));
    }

    private User save(User user) throws UserException {
        try {
            return dao.save(user);
        } catch (Exception ex) {
            throw new UserException(ex.getMessage());
        }
    }

    public User activeToggle(UserDto dto) throws UserException {
        User user = parseDtoToModel(dto);
        user.setActive(!user.isActive());
        return save(user);
    }

    private User parseDtoToModel(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .cellphone(dto.getCellphone())
                .city(dto.getCity())
                .homeNumber(dto.getHomeNumber())
                .name(dto.getName())
                .street(dto.getStreet())
                .zipCode(dto.getZipCode())
                .type(EnumUserType.getFromString(dto.getType()))
                .numberOfPrincipalDocument(dto.getNumberOfPrincipalDocument())
                .build();

    }
    public String authenticate(String credentials) throws UserAuthenticationException {
        Pair<String, String> userNameAndPassword = authService.extractUserNameAndPassword(credentials);
        User userAuth = dao.findFirstByUsername(userNameAndPassword.getFirst());
        if (userAuth == null) {
            throw new UserAuthenticationException("invalid user, verify your credentials");
        } else if (passwordEncoder.matches(userNameAndPassword.getSecond(),
                userAuth.getPassword())) {
            updateLastLogin(userAuth);

            return jwtGenerator.generate(userAuth);
        }
        throw new UserAuthenticationException("invalid user, verify your credentials");
    }

    private void updateLastLogin(User user) {
        user.setLastLogin(clock.now());
        dao.save(user);
    }

    public Optional<User> loadUserById(long id) {
        return dao.findById(id);
    }

    public Optional<User> loadUserBynumberOfPrincipalDocument(String document) {
        return dao.findByNumberOfPrincipalDocument(document);
    }
}
