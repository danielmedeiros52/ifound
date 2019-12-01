package com.Ifound.util;


import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.model.IFoundUserDetails;
import com.Ifound.model.User;
import com.Ifound.services.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret}")
    private String secret;
    private Clock clock = DefaultClock.INSTANCE;
    @Autowired
    private UserDetailService userDetailsService;

    public Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    public User getUserFromToken(String token) throws UserAuthenticationException {
        Claims body = getAllClaimsFromToken(token);
        return new User(Integer.parseInt((String) body.get("userID")) ,body.getSubject());

    }


    public String getUsernameFromToken(String token) throws UserAuthenticationException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) throws UserAuthenticationException {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) throws UserAuthenticationException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws UserAuthenticationException {


        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws UserAuthenticationException {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            throw new UserAuthenticationException("Invalid Token, please make login again.");
        }

    }

    private Boolean isTokenExpired(String token) throws UserAuthenticationException {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean isCreatedBeforeLastLogin(Date created, Date lastLogin) {
        return (lastLogin != null && created.before(lastLogin));
    }

    private Boolean ignoreTokenExpiration(String token) {
        return false;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) throws UserAuthenticationException {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean canTokenBeNewLogin(String token, Date lastLogin) throws UserAuthenticationException {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastLogin(created, lastLogin)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) throws UserAuthenticationException {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Boolean validExpiration(String token) throws UserAuthenticationException {
        if (token.isEmpty()) return false;
        IFoundUserDetails userDetails = (IFoundUserDetails) userDetailsService.loadUserByUsername(getUserNameFromJwtToken(token));
        final Date created = getIssuedAtDateFromToken(token);
        final Date lastLogin = userDetails.getLastLogin();
        final Date lastPasswordReset = userDetails.getLastPasswordResetDate();
        if (!isCreatedBeforeLastLogin(lastLogin, created)
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
        ) {
            return true;
        } else {
            logger.error("Token expired");
            return false;
        }
    }
}
