package com.uas.kelompoksatu.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uas.kelompoksatu.dontdelete.TokenResponse;
import com.uas.kelompoksatu.dontdelete.WebResponse;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.model.LoginUserRequest;
import com.uas.kelompoksatu.user.service.AuthUserService;

@RestController
@RequestMapping("/api/user/auth")
@CrossOrigin("*")
public class AuthUserController {

    @Autowired
    private AuthUserService authService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @DeleteMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> logout(User user) {
        authService.logout(user);
        return WebResponse.<String>builder().data("OK").build();
    }
}
