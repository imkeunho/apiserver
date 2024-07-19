package com.junjin.apiserver.service;

import com.junjin.apiserver.domain.Cart;
import com.junjin.apiserver.domain.CartItem;
import com.junjin.apiserver.domain.Member;
import com.junjin.apiserver.domain.Product;
import com.junjin.apiserver.dto.CartItemDTO;
import com.junjin.apiserver.dto.CartItemListDTO;
import com.junjin.apiserver.repository.CartItemRepository;
import com.junjin.apiserver.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {

        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int qty = cartItemDTO.getQty();
        long cino = cartItemDTO.getCino();

        //기존에 담겨있는 상품에 대한 처리
        if (cino != 0L) {

            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);

            CartItem cartItem = cartItemResult.orElseThrow();

            cartItem.changeQty(qty);

            cartItemRepository.save(cartItem);

            return getCartItems(email);
        }

        Cart cart = getCart(email);

        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if (cartItem == null) {

            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();

        } else {

            cartItem.changeQty(qty);
        }

        cartItemRepository.save(cartItem);

        return getCartItems(email);
    }

    private Cart getCart(String email) {

        //해당 이메일의 장바구니(Cart)가 있는지 확인, 있으면 반환
        //없으면 Cart 객체 생성, 추가 후 반환

        Cart cart = null;

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        if (result.isEmpty()) {

            log.info("Cart of the member is not exist!");

            Member member = Member.builder().email(email).build();

            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);
        } else {

            cart = result.get();
        }

        return cart;
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {

        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {

        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cino);

        return cartItemRepository.getItemsOfCartDTOByCart(cno);
    }
}
