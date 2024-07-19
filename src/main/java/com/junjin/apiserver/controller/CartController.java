package com.junjin.apiserver.controller;

import com.junjin.apiserver.dto.CartItemDTO;
import com.junjin.apiserver.dto.CartItemListDTO;
import com.junjin.apiserver.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("#cartItemDTO.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO cartItemDTO) {

        log.info(cartItemDTO);

        if (cartItemDTO.getQty() <= 0) {

            return cartService.remove(cartItemDTO.getCino());
        }

        return cartService.addOrModify(cartItemDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal) {

        String email = principal.getName();

        log.info("email : {}", email);

        return cartService.getCartItems(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {

        log.info("cart item no : {}", cino);

        return cartService.remove(cino);
    }
}
