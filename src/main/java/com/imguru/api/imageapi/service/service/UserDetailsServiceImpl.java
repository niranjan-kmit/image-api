package com.imguru.api.imageapi.service.service;


import com.imguru.api.imageapi.service.model.AuthUser;
import com.imguru.api.imageapi.service.model.UserEntity;
import com.imguru.api.imageapi.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            AuthUser authUser = new AuthUser(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
            return authUser;
        }
        return null;
    }
}
