package com.uas.kelompoksatu.member.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateMemberRequest {

    private String username;

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String password;

    @Size(max = 20)
    private String phoneNumber;
}
