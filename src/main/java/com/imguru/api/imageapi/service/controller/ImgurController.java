package com.imguru.api.imageapi.service.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.imguru.api.imageapi.service.service.UploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/image")
@AllArgsConstructor
@Slf4j
public class ImgurController {
    private UploadService uploadService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map> upload(@RequestParam(name = "image") MultipartFile image, @RequestHeader("image_info") String metaInfo) throws IOException {
        String base64Img = Base64.getEncoder().encodeToString(image.getBytes());
        Map uploadImage = uploadService.uploadImage(base64Img, metaInfo);
        return ResponseEntity.ok().body(uploadImage);
    }

    @GetMapping("/{imageId}")
    @ResponseBody
    public ResponseEntity<Map> getImage(@PathVariable("imageId") String imageId) {
        return ResponseEntity.ok().body(uploadService.getImage(imageId));
    }

    @PostMapping("/{imageId}")
    @ResponseBody
    public ResponseEntity<Map> updateImage(@PathVariable("imageId") String imageId, @RequestParam("title") String title, @RequestParam("description") String description) throws  JsonProcessingException {
        return ResponseEntity.ok().body(uploadService.updateImage(title, description, imageId));
    }

    @DeleteMapping("/{imageId}/delete")
    @ResponseBody
    public ResponseEntity<Map> deleteImage(@PathVariable("imageId") String imageId) {
        return ResponseEntity.ok().body(uploadService.deleteImage(imageId));
    }


}