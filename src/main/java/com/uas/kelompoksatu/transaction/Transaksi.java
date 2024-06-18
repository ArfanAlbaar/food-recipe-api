package com.uas.kelompoksatu.transaction;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uas.kelompoksatu.member.Member;
import com.uas.kelompoksatu.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double amount;

    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "member_usn", referencedColumnName = "username")
    private Member member;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

}
