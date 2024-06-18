package com.uas.kelompoksatu.member.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.ValidationService;
import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.MemberRepository;
import com.uas.kelompoksatu.member.model.MemberResponse;
import com.uas.kelompoksatu.member.model.RegisterMemberRequest;
import com.uas.kelompoksatu.member.model.UpdateMemberRequest;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.UserRepository;
import com.uas.kelompoksatu.user.security.BCrypt;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void register(RegisterMemberRequest request) {
        validationService.validate(request);

        if (memberRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        member.setName(request.getName());
        member.setPhoneNumber(request.getPhoneNumber());

        memberRepository.save(member);
    }

    public MemberResponse get(Member member) {
        return MemberResponse.builder()
                .username(member.getUsername())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }

    public List<Member> readAll(User user) {
        validationService.validate(user);

        User users = userRepository.findById(user.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (users.getToken() != null) {
            return memberRepository.findAll();
        }
        return null;
    }

    @Transactional
    public MemberResponse update(User user, UpdateMemberRequest update) {
        validationService.validate(update);
        if (user != null) {
            if (user.getToken() != null) {

                Member member = memberRepository.findById(update.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "member is not found"));

                log.info("REQUEST : {}", update);

                if (Objects.nonNull(update.getName())) {
                    member.setName(update.getName());
                }

                if (Objects.nonNull(update.getPassword())) {
                    member.setPassword(BCrypt.hashpw(update.getPassword(), BCrypt.gensalt()));
                }

                if (Objects.nonNull(update.getPhoneNumber())) {
                    member.setPhoneNumber(update.getPhoneNumber());
                }

                member.setPremium(update.getPremium());

                memberRepository.save(member);

                log.info("MEMBER : {}", member.getName());

                return MemberResponse.builder()
                        .username(member.getUsername())
                        .name(member.getName())
                        .phoneNumber(member.getPhoneNumber())
                        .premium(member.getPremium())
                        .build();

            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");

            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have permission");
        }

    }

    @Transactional
    public String delete(String username) {

        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "member not found"));
        memberRepository.deleteById(member.getUsername());

        return "Deleted";
    }
}
