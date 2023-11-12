package com.imguru.api.imageapi.service;

import com.imguru.api.imageapi.service.model.User;
import com.imguru.api.imageapi.service.model.UserEntity;
import com.imguru.api.imageapi.service.repository.UserRepository;
import com.imguru.api.imageapi.service.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void initialize(){
        userDetailsService=new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void testLoadUserDetailsByname(){
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(buildUserEntity()));
        UserDetails userDetails= userDetailsService.loadUserByUsername("user");
        Assertions.assertNotNull(userDetails);
        Assertions.assertTrue(userDetails.getUsername().equals("user"));
    }

    @Test
    public void testIfLoadUserDetailsNotFound(){
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        UserDetails userDetails= userDetailsService.loadUserByUsername("user");
        Assertions.assertNull(userDetails);
    }

    private UserEntity buildUserEntity(){
        UserEntity user=new UserEntity();
        user.setUsername("user");
        user.setPassword("password");

        user.setImageIds(Arrays.asList("two"));
        return user;
    }

    private User buildUser(){
        return User.builder().name("user").password("password").imageIds(Arrays.asList("two")).build();
    }
}
