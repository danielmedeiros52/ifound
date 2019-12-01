package com.Ifound.authentication;


import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.util.BeanUtil;
import com.Ifound.util.JwtUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);
    @Autowired
    JwtUtils jwtUtils;
    @Value("${jwt.secret}")
    private  String secret ;

    public boolean validateJwtToken(String authToken) {
        try {
            if (authToken == null || authToken.isEmpty() || authToken.equals("")) {
                return false;
            }
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return BeanUtil.getBean(JwtUtils.class).validExpiration(authToken);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e.getMessage());
        } catch (UserAuthenticationException e) {
            e.printStackTrace();
        }

        return false;
    }}
