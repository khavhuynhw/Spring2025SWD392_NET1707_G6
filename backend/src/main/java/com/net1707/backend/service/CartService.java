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
    public String addToCart(Long productId, int quantity, List<CartItemDTO> cart) {
        ProductDTO productDTO = iProductService.getProductById(productId); // get ProductDTO
        Product product = new Product(); // create new  Product from DTO
        product.setProductID(productDTO.getProductID());
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());

        for (CartItemDTO item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return "Product quantity updated in cart";
            }
        }

        CartItemDTO cartItem = new CartItemDTO(productId, product.getProductName(), quantity, product.getPrice());
        cart.add(cartItem);

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
