package com.junjin.apiserver.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private long cino;

    private int qty;

    private String pname;

    private int price;

    private Long pno;

    public String imageFile;

    public CartItemListDTO(long cino, int qty, String pname, int price, Long pno, String imageFile) {
        this.cino = cino;
        this.imageFile = imageFile;
        this.pname = pname;
        this.price = price;
        this.pno = pno;
        this.qty = qty;
    }
}
