package com.Ifound.authentication;


import com.Ifound.model.User;
import com.Ifound.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int jwtExpiration;
    private Clock clock = DefaultClock.INSTANCE;

    public String generate(User user) {

        final Date createdDate = clock.now();
        final Date expirationDate = jwtUtils.calculateExpirationDate(createdDate);
        Claims claims = Jwts.claims()
                .setSubject(user.getUsername());
        claims.put("userID", String.valueOf(user.getId()));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}