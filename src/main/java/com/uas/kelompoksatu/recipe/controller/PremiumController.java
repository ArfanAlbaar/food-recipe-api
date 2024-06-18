package com.uas.kelompoksatu.recipe.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uas.kelompoksatu.recipe.entities.Premium;
import com.uas.kelompoksatu.recipe.services.PremiumService;
import com.uas.kelompoksatu.user.User;

@RestController
@RequestMapping("/api/premium")
@CrossOrigin
public class PremiumController {

    @Autowired
    private PremiumService service;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Premium create(@RequestHeader("API-TOKEN") String token, @RequestParam("file") MultipartFile file) {
        try {
            return service.create(token, file);
        } catch (IOException e) {
            throw new RuntimeException("Error saving document", e);
        }
    }

    @PutMapping(path = "/{premiumId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Premium> update(User user, @PathVariable("premiumId") Integer premiumId,
            @RequestBody Premium update) {
        update.setId(premiumId);
        return service.update(user, update);
    }

    @DeleteMapping(path = "/{premiumId}")
    public String delete(User user, @PathVariable("premiumId") Integer premiumId) {
        return service.delete(user, premiumId);
    }

}