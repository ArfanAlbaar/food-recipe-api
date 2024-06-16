package com.uas.kelompoksatu.transaction;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.MemberRepository;
import com.uas.kelompoksatu.user.service.ValidationService;

@Service
public class TransaksiService {

    @Autowired
    private TransaksiRepository repo;
    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private ValidationService validationService;

    public String recordTransaksi(Member member, Transaksi input) {
        validationService.validate(input);
        if (member != null) {
            member.setPremium(true);
            Transaksi transaction = new Transaksi();
            transaction.setMember(member);
            transaction.setAmount(input.getAmount());
            transaction.setTimestamp(LocalDateTime.now());
            repo.save(input);
            member.setTransaction(transaction);

            memberRepo.save(member);
            return "Subscribed " + member.getUsername() + " to premium.";

        } else {
            return "Unauthorized action.";
        }
    }

}
