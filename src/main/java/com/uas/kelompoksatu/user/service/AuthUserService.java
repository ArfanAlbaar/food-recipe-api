package com.uas.kelompoksatu.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.TokenResponse;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.UserRepository;
import com.uas.kelompoksatu.user.model.LoginUserRequest;
import com.uas.kelompoksatu.user.security.BCrypt;

@Service
public class AuthUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (user.getToken() != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username already in use");

        } else if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
    }

    // Fungsi expiredAt menghitung waktu kedaluwarsa (expired time)
    // dalam milidetik (milliseconds) dari waktu saat ini. Berikut adalah
    // langkah-langkah perhitungannya:
    // Ini adalah fungsi yang mengembalikan waktu saat ini dalam
    // bentuk milidetik (milliseconds) sejak epoch (1 Januari 1970 00:00:00 UTC).
    // Jadi, (1000 * 60 * 60 * 24 * 1) = 1000 * 60 * 60 * 24 = 86.400.000 milidetik
    // (1 hari).
    // Untuk konversi dari milidetik ke hari, kita bagi jumlah milidetik dengan
    // jumlah milidetik dalam satu hari.
    // 86400000 milidetik / 86400000 milidetik per hari = 1 hari
    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1); // 30 untuk 30 hari, disini ganti 1 untuk 1 hari
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }
}
