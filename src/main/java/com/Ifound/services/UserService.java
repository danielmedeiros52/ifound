package com.Ifound.services;

import com.Ifound.dao.UserDAO;
import com.Ifound.dto.UserDto;
import com.Ifound.enumerates.EnumUserType;
import com.Ifound.exception.IFoundAuthenticationException;
import com.Ifound.exception.UserException;
import com.Ifound.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AuthService authService;
    private final UserDAO dao;

    @Autowired
    public UserService(AuthService authService, UserDAO dao) {
        this.authService = authService;
        this.dao = dao;
    }

    public User register(UserDto dto) throws IFoundAuthenticationException {
        if(authService.verifyUserToRegister(dto));
            return save(dto);
    }
    public  User update(UserDto dto) throws  UserException{
        try {
            return  save(dto);
        }catch (Exception ex){
            throw new UserException(ex.getMessage());
        }
    }

    private User save(UserDto dto) {
      return  save(parseDtoToModel(dto));
    }
    private User save(User user) {
      return   dao.save(user);
    }
    public User activeToggle(UserDto dto){
        User user = parseDtoToModel(dto);
        user.setActive(!user.isActive());
        return save(user);
    }
    private User parseDtoToModel(UserDto dto){
       return  User.builder()
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
}
