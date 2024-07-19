package com.junjin.apiserver.dto;

import lombok.Data;

@Data
public class MemberModifyDTO {

    private String email, pw, nickname;
}
