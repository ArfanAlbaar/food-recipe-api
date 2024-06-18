package com.uas.kelompoksatu.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private String name;

    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

}
