package com.junjin.apiserver.dto;

import lombok.Data;

@Data
public class CartItemDTO {

    private String email;

    private long pno, cino;

    private int qty;


}
