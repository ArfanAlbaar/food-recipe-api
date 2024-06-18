package com.uas.kelompoksatu.recipe.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.service.AuthMemberService;
import com.uas.kelompoksatu.recipe.entities.Premium;
import com.uas.kelompoksatu.recipe.entities.PremiumResponse;
import com.uas.kelompoksatu.recipe.services.PremiumService;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.service.AuthUserService;

@RestController
@RequestMapping("/api/premium/auth")
@CrossOrigin
public class AuthPremiumController {

    @Autowired
    private PremiumService service;

    @Autowired
    private AuthMemberService authMemberService;

    @Autowired
    private AuthUserService authUserService;

    @GetMapping(path = "/member/list")
    public List<PremiumResponse> listForMember(@RequestHeader("X-API-TOKEN") String token) {
        Member currentMember = authMemberService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));
        if (currentMember.getToken() != null) {
            return service.readAll();
        } else {
            return null;
        }

    }

    @GetMapping(path = "/member/{premiumId}")
    public Optional<PremiumResponse> readByIdForMember(@RequestHeader("X-API-TOKEN") String token,
            @PathVariable("premiumId") Integer premiumId) {
        Member currentMember = authMemberService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));

        if (currentMember.getToken() != null) {
            return service.readByIdForMember(premiumId);
        } else {
            return null;
        }
    }

    @GetMapping("/member/{premiumId}/download")
    public ResponseEntity<Resource> downloadPremiumFileMember(@RequestHeader("X-API-TOKEN") String token,
            @PathVariable("premiumId") Integer premiumId) {
        Member currentMember = authMemberService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));

        Premium premium = service.readById(premiumId);
        if (premium != null && currentMember.getPremium()) {
            ByteArrayResource resource = new ByteArrayResource(premium.getDataRecipes());

            // Set content disposition to attachment to trigger download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + getEncodedFilename(premium.getPremiumName()) + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            // Premium entity with given premiumId not found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/admin/list")
    public List<PremiumResponse> listForAdmin(@RequestHeader("API-TOKEN") String token) {
        User currentUser = authUserService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));
        if (currentUser.getToken() != null) {
            return service.readAll();
        } else {
            return null;
        }
    }

    @GetMapping(path = "/admin/{premiumId}")
    public Premium readByIdForAdmin(@RequestHeader("API-TOKEN") String token,
            @PathVariable("premiumId") Integer premiumId) {
        User currentUser = authUserService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));
        Premium premium = service.readById(premiumId);
        if (premium != null && currentUser.getToken() != null) {
            return service.readById(premiumId);
        } else {
            return null;
        }
    }

    @GetMapping("/admin/{premiumId}/download")
    public ResponseEntity<Resource> downloadPremiumFileAdmin(@RequestHeader("API-TOKEN") String token,
            @PathVariable("premiumId") Integer premiumId) {

        User currentUser = authUserService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));
        Premium premium = service.readById(premiumId);
        if (premium != null && currentUser.getToken() != null) {
            ByteArrayResource resource = new ByteArrayResource(premium.getDataRecipes());

            // Set content disposition to attachment to trigger download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + getEncodedFilename(premium.getPremiumName()) + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            // Premium entity with given premiumId not found
            return ResponseEntity.notFound().build();
        }
    }

    private String getEncodedFilename(String filename) {
        try {
            return URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Error encoding file name", ex);
        }
    }

}
