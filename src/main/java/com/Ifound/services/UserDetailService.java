package com.Ifound.services;


import com.Ifound.dao.UserDAO;
import com.Ifound.model.IFoundUserDetails;
import com.Ifound.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class UserDetailService implements UserDetailsService {

    private UserDAO userDao;

    @Autowired
    public UserDetailService(UserDAO userDao) {

        this.userDao = userDao;
    }

    public UserDetailService() {
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
        return IFoundUserDetails.build(user);  }
}
