package com.uas.kelompoksatu.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.model.LoginMemberRequest;
import com.uas.kelompoksatu.member.service.AuthMemberService;
import com.uas.kelompoksatu.user.model.TokenResponse;
import com.uas.kelompoksatu.user.model.WebResponse;

@RestController
@RequestMapping("/api/member/auth")
@CrossOrigin
public class AuthMemberController {

    @Autowired
    private AuthMemberService authMemberService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> login(@RequestBody LoginMemberRequest request) {
        TokenResponse tokenResponse = authMemberService.login(request);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    // @DeleteMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    // public WebResponse<String> logout(Member member) {
    // authMemberService.logout(member);
    // return WebResponse.<String>builder().data("OK").build();
    // }

    @DeleteMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> logout(@RequestHeader("X-API-TOKEN") String token) {
        System.out.print("X-API-TOKEN header received: {" + token + "}");
        Member currentMember = authMemberService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));
        if (currentMember != null) {
            authMemberService.logout(currentMember);
            return WebResponse.<String>builder().data("OK").build();
        } else {
            return WebResponse.<String>builder().data("Invalid token").build();
        }
    }
}
