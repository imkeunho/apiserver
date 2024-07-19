package com.junjin.apiserver.service;

import com.junjin.apiserver.dto.CartItemDTO;
import com.junjin.apiserver.dto.CartItemListDTO;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface CartService {

    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    List<CartItemListDTO> getCartItems(String email);

    List<CartItemListDTO> remove(Long cino);

}
