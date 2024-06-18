package com.uas.kelompoksatu.member.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.TokenResponse;
import com.uas.kelompoksatu.dontdelete.ValidationService;
import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.MemberRepository;
import com.uas.kelompoksatu.member.model.LoginMemberRequest;
import com.uas.kelompoksatu.user.security.BCrypt;

@Service
public class AuthMemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginMemberRequest request) {
        validationService.validate(request);

        Member member = memberRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (member.getToken() != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username already in use");

        } else if (BCrypt.checkpw(request.getPassword(), member.getPassword())) {
            member.setToken(UUID.randomUUID().toString());
            member.setTokenExpiredAt(next30Days());
            memberRepository.save(member);

            return TokenResponse.builder()
                    .token(member.getToken())
                    .expiredAt(member.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1); // 30 untuk 30 hari, disini ganti 1 untuk 1 hari
    }

    @Transactional
    public void logout(Member member) {
        member.setToken(null);
        member.setTokenExpiredAt(null);
        member.setLastLogin(LocalDateTime.now());

        memberRepository.save(member);
    }

    public Optional<Member> findByToken(String token) {
        return memberRepository.findFirstByToken(token); // Assuming you have this method in your repository
    }

}
