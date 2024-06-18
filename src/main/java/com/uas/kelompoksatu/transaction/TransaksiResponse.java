package com.uas.kelompoksatu.transaction;

import java.time.LocalDateTime;

import com.uas.kelompoksatu.member.Member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransaksiResponse {

    private Integer id;

    private double amount;

    private LocalDateTime timestamp;

    private Member member;

}
