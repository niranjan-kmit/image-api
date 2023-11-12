package com.imguru.api.imageapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imguru.api.imageapi.service.config.ImgurConfig;
import com.imguru.api.imageapi.service.model.User;
import com.imguru.api.imageapi.service.model.UserEntity;
import com.imguru.api.imageapi.service.producer.EventProducer;
import com.imguru.api.imageapi.service.service.UploadService;
import com.imguru.api.imageapi.service.service.UserService;
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
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {
    @Mock
    private  ImgurConfig imgurConfig;
    @Mock
    private  ObjectMapper objectMapper;
    @Mock
    private  RestTemplate restTemplate;
    @Mock
    private  UserService userService;

    private UploadService uploadService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContextImpl securityContext;

    @Mock
    private EventProducer eventProducer;

    @BeforeEach
    public void initialization(){
        uploadService=new UploadService(imgurConfig,restTemplate,userService,eventProducer);
    }

    @Test
    public void testUploadImage() throws JsonProcessingException {
        LinkedHashMap<String,Object> data=new LinkedHashMap<>();
        data.put("id","ujskjkkl");
        data.put("title","Image Title");
        data.put("description","Image Description");
        LinkedHashMap<String,Object> response=new LinkedHashMap<>();
        response.put("data",data);
        Mockito.when(imgurConfig.getAccessToken()).thenReturn("mxnclkch122435355");
        Mockito.when(imgurConfig.getUploadUrl()).thenReturn("https://localhost:8080/imgur/image");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("user");
        SecurityContextHolder.setContext(securityContext);
        try (MockedStatic<SecurityContextHolder> utilities = Mockito.mockStatic(SecurityContextHolder.class)) {
            utilities.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        }
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(),Mockito.any(HttpEntity.class),Mockito.eq(Map.class))).thenReturn(ResponseEntity.ok(response));
        Mockito.when(userService.loadUserDetails(Mockito.anyString())).thenReturn(Optional.of(buildUserEntity()));
        Mockito.when(userService.updateUser(Mockito.any(UserEntity.class))).thenReturn(buildUser());
        doNothing().when(eventProducer).publishProfileEventToTopic(Mockito.any(String.class));
        Map<String,Object> map=uploadService.uploadImage("86da8fc7f0ab6b9297b760f16623fd2ff6be752d","{\"title\":\"ImageTitle\",\"description\":\"Image Description\"}");
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.containsValue("ujskjkkl"));
    }

    @Test
    public void testGetImage() throws JsonProcessingException {
        LinkedHashMap<String,Object> data=new LinkedHashMap<>();
        data.put("id","ujskjkkl");
        data.put("title","Image Title");
        data.put("description","Image Description");
        LinkedHashMap<String,Object> response=new LinkedHashMap<>();
        response.put("data",data);
        Mockito.when(imgurConfig.getAccessToken()).thenReturn("mxnclkch122435355");
        Mockito.when(imgurConfig.getUploadUrl()).thenReturn("https://localhost:8080/imgur/image");
        Mockito.when(restTemplate.exchange(Mockito.anyString(),Mockito.any(HttpMethod.class),Mockito.any(HttpEntity.class),Mockito.any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(response));
        Map<String,Object> map=uploadService.getImage("ujskjkkl");
        Assertions.assertNotNull(map);
        Map<String,Object> dataMap= (Map<String, Object>) map.get("data");
        Assertions.assertTrue(dataMap.containsValue("Image Title"));
        Assertions.assertTrue(dataMap.containsValue("Image Description"));

    }

    @Test
    public void testDeleteImage() throws JsonProcessingException {
        LinkedHashMap<String,Object> data=new LinkedHashMap<>();
        data.put("data","true");
        data.put("deleted","true");
        data.put("status","200");
        Mockito.when(imgurConfig.getAccessToken()).thenReturn("mxnclkch122435355");
        Mockito.when(imgurConfig.getUploadUrl()).thenReturn("https://localhost:8080/imgur/image");
        Mockito.when(restTemplate.exchange(Mockito.anyString(),Mockito.any(HttpMethod.class),Mockito.any(HttpEntity.class),Mockito.any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(data));
        Map<String,Object> map=uploadService.deleteImage("ujskjkkl");
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.get("deleted").equals("true"));
        Assertions.assertTrue(map.get("status").equals("200"));

    }

    private UserEntity buildUserEntity(){
        UserEntity user=new UserEntity();
        user.setUsername("user");
        user.setPassword("password");
        user.setImageIds(new ArrayList<>());
        return user;
    }

    private User buildUser(){
        return User.builder().name("user").password("password").imageIds(Arrays.asList("ujskjkkl")).build();
    }
}
