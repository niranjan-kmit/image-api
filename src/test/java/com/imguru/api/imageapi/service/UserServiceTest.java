package com.imguru.api.imageapi.service;

import com.imguru.api.imageapi.service.config.ImgurConfig;
import com.imguru.api.imageapi.service.model.User;
import com.imguru.api.imageapi.service.model.UserEntity;
import com.imguru.api.imageapi.service.repository.UserRepository;
import com.imguru.api.imageapi.service.service.UserService;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  RestTemplate restTemplate;
    @Mock
    private  ImgurConfig imgurConfig;

    private UserService userService;

    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContextImpl securityContext;
    @BeforeEach
    public void initialize(){
        userService=new UserService(userRepository,passwordEncoder,restTemplate,imgurConfig);
    }
@Test
    public void testListMyprofileImages(){
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("user");
        SecurityContextHolder.setContext(securityContext);
        try (MockedStatic<SecurityContextHolder> utilities = Mockito.mockStatic(SecurityContextHolder.class)) {
            utilities.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        }
        Mockito.when(imgurConfig.getAccessToken()).thenReturn("mxnclkch122435355");
        Mockito.when(restTemplate.exchange(Mockito.anyString(),Mockito.any(HttpMethod.class),Mockito.any(HttpEntity.class),Mockito.any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(getAllImages()));
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(buildUserEntity()));
        Map<String,Object> map=userService.listMyProfileImages();
        Assertions.assertNotNull(map);
       List<LinkedHashMap<String,Object>> dataMap= (List<LinkedHashMap<String, Object>>) map.get("data");
        Assertions.assertTrue(dataMap.size()==1);
    }

    @Test
    public void testSaveUser(){
        Mockito.when(passwordEncoder.encode(Mockito.any(CharSequence.class))).thenReturn("f1d6d3b2434b315");
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(buildUserEntity());
        User user=userService.saveUser(buildUser());
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getImageIds().contains("two"));
        Assertions.assertTrue(user.getName().equals("user"));
    }

    @Test
    public void testupdateUser(){
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(buildUserEntity());
        User user=userService.updateUser(buildUserEntity());
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getImageIds().contains("two"));
        Assertions.assertTrue(user.getName().equals("user"));
    }

    private LinkedHashMap<String,Object> getAllImages(){
        LinkedHashMap<String,Object> data=new LinkedHashMap<>();
        List<LinkedHashMap<String,Object>> hashMapList=new ArrayList<>();
        LinkedHashMap<String,Object> img1=new LinkedHashMap<>();
        img1.put("id","one");
        img1.put("title","Image Title1");
        img1.put("description","Image Description1");
        hashMapList.add(img1);

        LinkedHashMap<String,Object> img2=new LinkedHashMap<>();
        img2.put("id","two");
        img2.put("title","Image Title2");
        img2.put("description","Image Description2");
        hashMapList.add(img2);
        LinkedHashMap<String,Object> img3=new LinkedHashMap<>();

        img3.put("id","three");
        img3.put("title","Image Title3");
        img3.put("description","Image Description3");
        hashMapList.add(img3);
        data.put("data",hashMapList);
        return data;
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
