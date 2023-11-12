package com.imguru.api.imageapi.service.service;

import com.imguru.api.imageapi.service.config.ImgurConfig;
import com.imguru.api.imageapi.service.model.User;
import com.imguru.api.imageapi.service.model.UserEntity;
import com.imguru.api.imageapi.service.repository.UserRepository;
import com.imguru.api.imageapi.service.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final ImgurConfig imgurConfig;

    public UserService(@Lazy UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RestTemplate restTemplate, ImgurConfig imgurConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.imgurConfig = imgurConfig;
    }

    public boolean isUserExists(String userName) {
        return userRepository.existsByUsername(userName);
    }

    public Optional<UserEntity> loadUserDetails(String userName) {
        return userRepository.findByUsername(userName);
    }

    public User saveUser(User registerDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getName());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));
        user.setImageIds(new ArrayList<>());
        UserEntity user1=userRepository.save(user);
        log.info("user details saved {}",user1);
        return registerDto;
    }

    public User updateUser(UserEntity userEntity) {
        UserEntity user=userRepository.save(userEntity);
        log.info("user details updated {}",user);
        return UserUtils.wrapUserEntityToUser(user);
    }

    public Map listMyProfileImages() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.add("Authorization", "Bearer " + imgurConfig.getAccessToken());
        String requestUrl = "https://api.imgur.com/3/account/me/images";
        HttpEntity<String> entity = new HttpEntity<String>(header);
        ResponseEntity<Map> response = restTemplate.exchange(requestUrl,
                HttpMethod.GET, entity, new ParameterizedTypeReference<Map>() {
                });
        LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
        List<LinkedHashMap<String, Object>> hashMapList = (List<LinkedHashMap<String, Object>>) resBody.get("data");
        log.info("all the image profile data from primary imguru account {}",hashMapList);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> userEntity = userRepository.findByUsername(authentication.getName());
        List<LinkedHashMap<String, Object>> linkedHashMaps = new ArrayList<>();
        Map<String, Object> objectMap = new HashMap<>();

        if (userEntity.isPresent()) {
            objectMap.put("profile", userEntity.get().getUsername());
            List<String> imageIds = userEntity.get().getImageIds();
            imageIds.forEach(imgId -> {
                hashMapList.stream().forEach(map -> {
                    if (map.containsValue(imgId)) {
                        linkedHashMaps.add(map);
                    }
                });
            });
            objectMap.put("data", linkedHashMaps);
        }
        log.info("Image profile data : {} with user profile :{}",objectMap,authentication.getName());
        return objectMap;
    }
}
