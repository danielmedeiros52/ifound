package com.Ifound.services;

import com.Ifound.dao.UserDAO;
import com.Ifound.dto.UserDto;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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

    public Pair<String, String> extractUserNameAndPassword(String strInBase64) throws UserAuthenticationException {
        try {
            return Pair.of(
            new String (Base64.decodeBase64(strInBase64.getBytes())).split(":")[0],
            new String (Base64.decodeBase64(strInBase64.getBytes())).split(":")[1]
            );
        }catch (Exception e){
                throw new UserAuthenticationException("You need send credentials in the correct format.");
        }


    }


}
