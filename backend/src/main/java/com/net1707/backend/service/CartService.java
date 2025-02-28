package com.net1707.backend.service;

import com.net1707.backend.dto.CartItemDTO;
import com.net1707.backend.dto.ProductDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.service.Interface.ICartService;
import com.net1707.backend.service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartService implements ICartService {

    @Autowired
    private IProductService iProductService;


    @Override
    public String addToCart(CartItemDTO cartItemDTO, List<CartItemDTO> cart) {
        // Lấy thông tin sản phẩm từ ProductService
        ProductDTO productDTO = iProductService.getProductById(cartItemDTO.getProductId());

        if (productDTO == null) {
            return "Product not found";
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        for (CartItemDTO item : cart) {
            if (item.getProductId().equals(cartItemDTO.getProductId())) {
                item.setQuantity(item.getQuantity() + cartItemDTO.getQuantity());
                return "Product quantity updated in cart";
            }
        }

        // Thêm sản phẩm mới vào giỏ hàng
        cart.add(new CartItemDTO(cartItemDTO.getProductId(), cartItemDTO.getQuantity()));

        return "Product added to cart";
    }





    @Override
    public String removeFromCart(Long productId, List<CartItemDTO> cart) {
        boolean removed = cart.removeIf(item -> Objects.equals(item.getProductId(), productId));
        return removed ? "Product removed from cart" : "Product not found in cart";
    }

    @Override
    public String clearCart(SessionStatus status) {
        status.setComplete();
        return "Cart cleared";
    }
}
