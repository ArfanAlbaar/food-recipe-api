package com.uas.kelompoksatu.member;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uas.kelompoksatu.transaction.Transaksi;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "members")
public class Member {

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    private LocalDateTime lastLogin;

    private Boolean premium;

    @JsonIgnore
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Transaksi transaction;
}