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

@RestController
@RequestMapping("/api/premium")
@CrossOrigin("*")
public class PremiumController {

    @Autowired
    private PremiumService service;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public String create(@RequestHeader("API-TOKEN") String token, @RequestParam("file") MultipartFile file) {
        try {
            service.create(token, file);
            return "Success";
        } catch (IOException e) {
            throw new RuntimeException("Error saving document", e);
        }
    }

    @PutMapping(path = "/{premiumId}")
    public ResponseEntity<Premium> update(
            @RequestHeader("API-TOKEN") String token,
            @PathVariable("premiumId") Integer premiumId,
            @RequestBody Premium update) {
        update.setId(premiumId);
        return service.update(token, update);
    }

    @DeleteMapping(path = "/{premiumId}")
    public String delete(@RequestHeader("API-TOKEN") String token, @PathVariable("premiumId") Integer premiumId) {
        return service.delete(token, premiumId);
    }

}