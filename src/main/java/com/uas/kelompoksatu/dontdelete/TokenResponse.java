package com.uas.kelompoksatu.dontdelete;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {

    private String token;

    private Long expiredAt;
}
