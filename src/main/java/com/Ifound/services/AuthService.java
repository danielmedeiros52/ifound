package com.Ifound.services;

import com.Ifound.dao.UserDAO;
import com.Ifound.dto.UserDto;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDAO userDAO;

    @Autowired
    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean verifyUserToRegister(UserDto dto) throws IFoundAuthenticationException {
        User user = userDAO.findByEmailOrNumberOfPrincipalDocument(dto.getEmail(), dto.getNumberOfPrincipalDocument());
        if (user != null) {
            throw new IFoundAuthenticationException("User already registered");
        }
        return true;
    }
}
