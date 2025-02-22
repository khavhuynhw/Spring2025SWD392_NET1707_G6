package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.CartItemDTO;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

public interface ICartService {

        String addToCart(int productId, int quantity, List<CartItemDTO> cart);

        String removeFromCart(int productId, List<CartItemDTO> cart);

        String clearCart(SessionStatus status);

}
