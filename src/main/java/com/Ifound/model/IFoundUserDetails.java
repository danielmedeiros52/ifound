package com.Ifound.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IFoundUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private static final long serialVersionUID = 1L;
    private final Date lastPasswordResetDate;
    private String userName;
    private String name;
    private String token;
    private long id;
    private boolean isAdministrator;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Date lastLogin;

    public IFoundUserDetails(long id, String name,
                             String username, String token, String password,
                             List<? extends GrantedAuthority> authorities, Date lastPasswordResetDate, boolean isAdministrator, Date lastLogin) {
        this.id = id;
        this.name = name;
        this.userName = username;
        this.token = token;
        this.password = password;
        this.authorities = authorities;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.isAdministrator = isAdministrator;
        this.lastLogin = lastLogin;
    }

    public static IFoundUserDetails build(User user) {


        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getDescription())
        ).collect(Collectors.toList());



        return new IFoundUserDetails(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getToken(),
                user.getPassword(),
                authorities,
                user.getLastPasswordResetDate(),
                user.isAdministrator(),
                user.getLastLogin()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IFoundUserDetails user = (IFoundUserDetails) o;
        return Objects.equals(id, user.id);
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public String getUserName() {
        return userName;
    }

    public Date getLastLogin() {
        return lastLogin;
    }
}