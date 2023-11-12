package com.imguru.api.imageapi.service.controller;

import com.imguru.api.imageapi.service.model.User;
import com.imguru.api.imageapi.service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (userService.isUserExists(user.getName())) {
            User user1 = User.builder().name(user.getName()).password(user.getPassword()).error("User Already taken").build();
            return new ResponseEntity<>(user1, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @GetMapping("/me/profile")
    public ResponseEntity<Map> listImagesForProfile() {
        return ResponseEntity.ok(userService.listMyProfileImages());
    }

    @GetMapping("/greeting")
    public String greetingMessage() {
        return "Hello Demo";
    }
}
