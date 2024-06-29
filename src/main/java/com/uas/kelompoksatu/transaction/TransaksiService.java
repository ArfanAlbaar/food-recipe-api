package com.uas.kelompoksatu.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.ValidationService;
import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.MemberRepository;
import com.uas.kelompoksatu.member.model.MemberResponse;
import com.uas.kelompoksatu.member.service.AuthMemberService;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.UserRepository;
import com.uas.kelompoksatu.user.service.AuthUserService;

import jakarta.transaction.Transactional;

@Service
public class TransaksiService {

    @Autowired
    private TransaksiRepository repo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private AuthMemberService authMemberService;
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private ValidationService validationService;

    public MemberResponse recordTransaksi(String token, Transaksi input) {
        validationService.validate(input);

        Member member = authMemberService.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First"));
        if (member.getToken().equals(token)) {
            member.setPremium(true);
            Transaksi transaction = new Transaksi();
            transaction.setMember(member);
            transaction.setAmount(input.getAmount());
            transaction.setTimestamp(LocalDateTime.now());

            member.setTransaction(transaction);
            memberRepo.save(member);

            return MemberResponse.builder()
                    .name(member.getName())
                    .phoneNumber(member.getPhoneNumber())
                    .premium(member.getPremium())
                    .build();
            // return "Subscribed " + member.getUsername() + " to premium.";

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }
    }

    public List<Transaksi> readAll(User user) {
        validationService.validate(user);
        User users = userRepo.findById(user.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (users.getToken() != null) {

            return repo.findAll();

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }
    }

    public Transaksi readTransaksiById(User user, Integer transactionId) {
        validationService.validate(user);
        User users = userRepo.findById(user.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (users.getToken() != null) {

            return repo.findById(transactionId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi not found"));

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

    @Transactional
    public ResponseEntity<Transaksi> update(User user, Transaksi update) {
        validationService.validate(user);
        validationService.validate(update);
        User users = userRepo.findById(user.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (users.getToken() != null) {
            Transaksi transaction = repo.findById(update.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi not found"));

            transaction.setAmount(update.getAmount());
            transaction.setTimestamp(LocalDateTime.now());
            repo.save(transaction);
            return new ResponseEntity<Transaksi>(transaction, HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

    @Transactional
    public String delete(String token, Integer transactionId) {
        User users = authUserService.findByToken(token)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));
        if (users.getToken().equals(token)) {

            Transaksi transaction = repo.findById(transactionId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi not found"));
            System.out.println("Deleting transaction with ID: " + transaction.getId());

            repo.deleteById(transaction.getId());
            return "Deleted";

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }
    }

}
