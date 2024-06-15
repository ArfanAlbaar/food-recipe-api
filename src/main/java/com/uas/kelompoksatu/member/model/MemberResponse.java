package com.uas.kelompoksatu.member.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponse {

    private String username;

    private String name;

    private String phoneNumber;
}
