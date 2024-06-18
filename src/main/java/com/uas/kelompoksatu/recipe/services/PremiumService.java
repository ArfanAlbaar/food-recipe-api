package com.uas.kelompoksatu.recipe.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.ValidationService;
import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.MemberRepository;
import com.uas.kelompoksatu.recipe.entities.Premium;
import com.uas.kelompoksatu.recipe.repositories.PremiumRepository;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.UserRepository;
import com.uas.kelompoksatu.user.service.AuthUserService;

import jakarta.transaction.Transactional;

@Service
public class PremiumService {

    @Autowired
    private PremiumRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AuthUserService authUserService;

    @Transactional
    public Premium create(String token, MultipartFile file) throws IOException {
        User users = authUserService.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));
        if (users.getToken().equals(token)) {

            Premium premium = new Premium();
            premium.setPremiumName(file.getOriginalFilename());
            premium.setDataRecipes(file.getBytes());
            return repo.save(premium);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

    public List<Premium> readAllPremiumForMember(Member member) {
        validationService.validate(member);
        Member members = memberRepo.findById(member.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (members.getToken() != null) {
            if (members.getPremium() == true) {
                return repo.findAll();
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please BUY PREMIUM First");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

    public Premium readById(Integer premiumId) {
        return repo.findById(premiumId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "premium not found"));
    }

    public Premium readPremiumByIdForMember(Member member, Integer premiumId) {
        validationService.validate(member);
        Member members = memberRepo.findFirstByToken(member.getToken())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (members.getToken() != null) {
            if (members.getPremium() == true) {
                return repo.findById(premiumId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please BUY PREMIUM First");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

    public List<Premium> readAllPremiumForAdmin(User user) {
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

    @Transactional
    public ResponseEntity<Premium> update(User user, Premium update) {
        validationService.validate(update);

        Premium premium = repo.findFirstByUserAndId(user, update.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "premium is not found"));
        premium.setPremiumName(update.getPremiumName());
        premium.setDataRecipes(update.getDataRecipes());
        repo.save(premium);
        return new ResponseEntity<Premium>(premium, HttpStatus.ACCEPTED);
    }

    @Transactional
    public String delete(User user, Integer premiumId) {
        validationService.validate(user);
        Premium premium = repo.findFirstByUserAndId(user, premiumId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "premium not found"));
        repo.deleteById(premium.getId());
        return "Deleted";
    }

}
