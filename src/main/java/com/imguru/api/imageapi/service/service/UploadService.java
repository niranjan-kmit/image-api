package com.imguru.api.imageapi.service.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imguru.api.imageapi.service.config.ImgurConfig;
import com.imguru.api.imageapi.service.model.ImageInfo;
import com.imguru.api.imageapi.service.model.UserEntity;
import com.imguru.api.imageapi.service.producer.EventProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class UploadService {
    private final ImgurConfig imgurConfig;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    private final UserService userService;
    private EventProducer eventProducer;

    public UploadService(ImgurConfig imgurConfig, RestTemplate restTemplate, UserService userService,EventProducer eventProducer) {
        this.imgurConfig = imgurConfig;
        this.objectMapper = new ObjectMapper();
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.eventProducer=eventProducer;
    }

    public Map<String, Object> uploadImage(String base64String, String metaInfo) throws  JsonProcessingException {
        String accessToken = imgurConfig.getAccessToken();
        if (Strings.isBlank(accessToken))
            throw new RuntimeException("Access token not found, need admin auth");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.add("Authorization", "Bearer " + accessToken);
        ImageInfo imageInfo = objectMapper.readValue(metaInfo, ImageInfo.class);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", imageInfo.getTitle());
        body.add("image", base64String);
        body.add("description", imageInfo.getDescription());
        log.info("image upload  metadata information : {}",body);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, header);
        log.info("Image upload request url:{}",imgurConfig.getUploadUrl());
        ResponseEntity<Map> response = restTemplate.postForEntity(imgurConfig.getUploadUrl(), request, Map.class);
        LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
        LinkedHashMap<String, Object> res = (LinkedHashMap<String, Object>) resBody.get("data");
        log.info("image upload done successfully : {}",res);
        // associate user with image id
        String imageId = (String) res.get("id");
        SecurityContextImpl securityContext= (SecurityContextImpl) SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<UserEntity> userEntity = userService.loadUserDetails(authentication.getName());
        UserEntity user = userEntity.get();
        List<String> imageIds = user.getImageIds();
        imageIds.add(imageId);
        user.setImageIds(imageIds);
        userService.updateUser(user);
        log.info("user profile updated with imageId: {}",imageId);
        //sending user profile event to kafka topic
        String message=objectMapper.writeValueAsString(user);
        eventProducer.publishProfileEventToTopic(message);
        return res;
    }

    public Map<String, Object> updateImage(String title, String description, String imageId) throws  JsonProcessingException {
        String accessToken = imgurConfig.getAccessToken();
        if (Strings.isBlank(accessToken))
            throw new RuntimeException("Access token not found, need admin auth");
        Map<String, Object> body = getImage(imageId);
        body.put("title", title);
        body.put("description", description);
        log.info("image upload  metadata information : {}",body);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> entry : body.entrySet())
            multiValueMap.put(entry.getKey(), Collections.singletonList(entry.getValue()));
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.add("Authorization", "Bearer " + accessToken);
        String requestUrl = imgurConfig.getUploadUrl() + "/" + imageId;
        log.info("Image update request url:{}",requestUrl);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, header);
        ResponseEntity<Map> response = restTemplate.postForEntity(requestUrl, request, Map.class);
        LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
        log.info("image update done successfully : {}",resBody);
        return resBody;
    }

    public Map<String, Object> getImage(String ImageId) {
        String accessToken = imgurConfig.getAccessToken();
        if (Strings.isBlank(accessToken))
            throw new RuntimeException("Access token not found, need admin auth");
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + accessToken);
        String requestUrl = imgurConfig.getUploadUrl() + "/" + ImageId;
        log.info("Image getRequest url:{}",requestUrl);
        HttpEntity<String> entity = new HttpEntity<String>(header);
        ResponseEntity<Map> response = restTemplate.exchange(requestUrl,
                HttpMethod.GET, entity, new ParameterizedTypeReference<Map>() {
                });
        Map<String, Object> res=response.getBody();
        log.info("Image metadata with Id: {} imageData:{}",ImageId,res);
        return res;
    }

    public Map<String, Object> deleteImage(String ImageId) {
        String accessToken = imgurConfig.getAccessToken();
        if (Strings.isBlank(accessToken))
            throw new RuntimeException("Access token not found, need admin auth");

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + accessToken);
        String requestUrl = imgurConfig.getUploadUrl() + "/" + ImageId;
        log.info("Image delete request url:{}",requestUrl);
        HttpEntity<String> entity = new HttpEntity<String>(header);
        ResponseEntity<Map> response = restTemplate.exchange(requestUrl,
                HttpMethod.DELETE, entity, new ParameterizedTypeReference<Map>() {
                });
        Map<String, Object> res=response.getBody();
        log.info("image deletion done successfully with ImageId: {} and res : {}",ImageId,res);
        return res;
    }
}