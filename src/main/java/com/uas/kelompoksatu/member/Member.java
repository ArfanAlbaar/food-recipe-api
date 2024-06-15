package com.uas.kelompoksatu.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uas.kelompoksatu.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_user", referencedColumnName = "username")
    private User user;

    // @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    // private Transaction transaction;
}