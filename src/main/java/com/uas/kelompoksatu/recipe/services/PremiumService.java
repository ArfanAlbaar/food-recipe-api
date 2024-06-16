package com.uas.kelompoksatu.recipe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.member.MemberRepository;
import com.uas.kelompoksatu.recipe.entities.Premium;
import com.uas.kelompoksatu.recipe.repositories.PremiumRepository;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.UserRepository;
import com.uas.kelompoksatu.user.service.ValidationService;

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

    @Transactional
    public Premium create(User user, Premium premium) {
        validationService.validate(premium);
        return repo.save(premium);
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

    public Premium readPremiumByIdForMember(Member member, Integer recipeId) {
        validationService.validate(member);
        Member members = memberRepo.findById(member.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (members.getToken() != null) {
            if (members.getPremium() == true) {
                return repo.findById(recipeId)
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

    public Premium readPremiumByIdForAdmin(User user, Integer recipeId) {
        validationService.validate(user);
        User users = userRepo.findById(user.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));

        if (users.getToken() != null) {

            return repo.findById(recipeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));

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
    public String delete(User user, Integer recipeId) {
        validationService.validate(user);
        Premium premium = repo.findFirstByUserAndId(user, recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "premium not found"));
        repo.deleteById(premium.getId());
        return "Deleted";
    }

}
