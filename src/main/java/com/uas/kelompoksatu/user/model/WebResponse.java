package com.uas.kelompoksatu.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebResponse<T> {

    private T data;

    private String errors;

    private PagingResponse paging;
}
