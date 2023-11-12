package com.imguru.api.imageapi.service.security;


import com.imguru.api.imageapi.service.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl myUserDetailsService;
    @Autowired
    private PasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("user making api call with userName:{}",username);
        UserDetails user = myUserDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("Details not found");
        }
        if (encoder.matches(password, user.getPassword())) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("user authentication done successfully and security context initialized with user:{}",username);
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Password mismatch");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
