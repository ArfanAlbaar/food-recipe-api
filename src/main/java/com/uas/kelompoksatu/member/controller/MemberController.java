package com.uas.kelompoksatu.member.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.WebResponse;
import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.model.MemberResponse;
import com.uas.kelompoksatu.member.model.RegisterMemberRequest;
import com.uas.kelompoksatu.member.model.UpdateMemberRequest;
import com.uas.kelompoksatu.member.service.MemberService;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.UserRepository;

@RestController
@RequestMapping("/api/member")
@CrossOrigin
public class MemberController {

    @Autowired
    private MemberService service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterMemberRequest request) {
        service.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<MemberResponse> get(Member member) {
        MemberResponse memberResponse = service.get(member);
        return WebResponse.<MemberResponse>builder().data(memberResponse).build();
    }

    @GetMapping("/list")
    public List<Member> getAll(User user) {
        return service.readAll(user);
    }

    // @PatchMapping(path = "/current", consumes = MediaType.APPLICATION_JSON_VALUE,
    // produces = MediaType.APPLICATION_JSON_VALUE)
    // public WebResponse<MemberResponse> update(User user, @RequestBody
    // UpdateMemberRequest request) {
    // MemberResponse memberResponse = service.update(user, request);
    // return WebResponse.<MemberResponse>builder().data(memberResponse).build();
    // }

    @PutMapping("/{username}")
    public WebResponse<MemberResponse> update(User user, @PathVariable("username") String username,
            @RequestBody UpdateMemberRequest member) {
        member.setUsername(username);
        MemberResponse response = service.update(user, member);
        return WebResponse.<MemberResponse>builder().data(response).build();
    }

    @DeleteMapping("/{username}")
    public String delete(@RequestHeader("API-TOKEN") String token, @PathVariable("username") String username) {
        // Fetch user by token
        Optional<User> optionalUser = userRepository.findFirstByToken(token);

        if (optionalUser.isPresent()) {
            return service.delete(username);
        } else {
            // Handle case where user with given token is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrator not found for the given token");
        }
    }

}
