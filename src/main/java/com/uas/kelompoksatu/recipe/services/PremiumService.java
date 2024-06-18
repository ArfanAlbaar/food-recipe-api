package com.uas.kelompoksatu.recipe.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.ValidationService;
import com.uas.kelompoksatu.member.MemberRepository;
import com.uas.kelompoksatu.recipe.entities.Premium;
import com.uas.kelompoksatu.recipe.entities.PremiumResponse;
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
    public String create(String token, MultipartFile file) throws IOException {
        User users = authUserService.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));
        if (users.getToken().equals(token)) {

            Premium premium = new Premium();
            premium.setPremiumName(file.getOriginalFilename());
            premium.setDataRecipes(file.getBytes());
            repo.save(premium);
            return "Success";

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

    public List<PremiumResponse> readAll() {
        return repo.findAllBy();
    }

    public Premium readById(Integer premiumId) {
        return repo.findById(premiumId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RECIPE IS NOT FOUND"));
    }

    public Optional<PremiumResponse> readByIdForMember(Integer premiumId) {
        return repo.findFirstById(premiumId);
    }

    @Transactional
    public ResponseEntity<Premium> update(String token, Premium update) {
        validationService.validate(update);

        User users = authUserService.findByToken(token)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));
        if (users.getToken().equals(token)) {

            Premium premium = repo.findById(update.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "premium is not found"));
            premium.setPremiumName(update.getPremiumName());
            // premium.setDataRecipes(update.getDataRecipes()); //GAUSAH UPDATE FILE,
            // CUMA UPDATE NAMA, HAPUS AJA VALUENYA KALO UPDATE FILE
            repo.save(premium);
            return new ResponseEntity<Premium>(premium, HttpStatus.ACCEPTED);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

    @Transactional
    public String delete(String token, Integer premiumId) {

        User users = authUserService.findByToken(token)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));
        if (users.getToken().equals(token)) {

            Premium premium = repo.findById(premiumId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "premium not found"));
            repo.deleteById(premium.getId());
            return "Deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }

}
