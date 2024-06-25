package com.uas.kelompoksatu.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.uas.kelompoksatu.member.model.MemberResponse;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.service.AuthUserService;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin("*")
public class TransaksiController {

    @Autowired
    private TransaksiService service;

    @Autowired
    private AuthUserService authUserService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberResponse create(@RequestHeader("X-API-TOKEN") String token, @RequestBody Transaksi input) {
        return service.recordTransaksi(token, input);
    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Transaksi> list(User user) {
        return service.readAll(user);
    }

    @GetMapping(path = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaksi readById(User user, @PathVariable("transactionId") Integer transactionId) {
        return service.readTransaksiById(user, transactionId);
    }

    @PutMapping(path = "/{transactionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaksi> update(User user, @PathVariable("transactionId") Integer transactionId,
            @RequestBody Transaksi transaction) {
        transaction.setId(transactionId);
        return service.update(user, transaction);
    }

    @DeleteMapping(path = "/{transactionId}")
    public String delete(@RequestHeader("API-TOKEN") String token,
            @PathVariable("transactionId") Integer transactionId) {

        User user = authUserService.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in"));
        if (user.getToken().equals(token)) {
            return service.delete(transactionId);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }

    }

}
