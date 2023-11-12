package com.imguru.api.imageapi.service.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private String name;
    private String password;

    private List<String> imageIds;

    //private List<Role> roles;
    private String error;


}