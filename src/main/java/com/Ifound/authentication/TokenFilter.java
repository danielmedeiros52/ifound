package com.Ifound.authentication;


import com.Ifound.exception.UserAuthenticationException;
import com.Ifound.services.UserDetailService;
import com.Ifound.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserDetailService userDetailsService;
    private final JwtValidator validator;
    private final String tokenHeader;
    private JwtUtils jwtUtils;

    public TokenFilter(UserDetailService userDetailsService, @Value("${jwt.header}") String tokenHeader, JwtValidator jwtValidator, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.validator = jwtValidator;
        this.tokenHeader = tokenHeader;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authToken = request.getHeader(tokenHeader);

        if (validator.validateJwtToken(authToken)) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtUtils.getUsernameFromToken(authToken));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("authorized user '{}', setting security context", userDetails.getUsername());
            } catch (IllegalArgumentException | UserAuthenticationException e) {
                logger.error("an error occurred during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            }
        } else {
            logger.warn("couldn't find valid header");
        }
                filterChain.doFilter(request, response);
        }
    }



