package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.CartItemDTO;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

public interface ICartService {

        String addToCart(Long userId, CartItemDTO cartItem);

        String removeFromCart(Long userId, Long productId);

        void clearCart(Long userId);

        List<CartItemDTO> getCartByUserId(Long userId);

        CartItemDTO updateQuantity(Long userId, Long productId, Integer quantity);



}
