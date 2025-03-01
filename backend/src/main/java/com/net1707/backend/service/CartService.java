package com.net1707.backend.service;

import com.net1707.backend.dto.CartItemDTO;
import com.net1707.backend.dto.ProductDTO;
import com.net1707.backend.service.Interface.ICartService;
import com.net1707.backend.service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class CartService implements ICartService {

    @Autowired
    private IProductService iProductService;


    // get cart form userid
    private final Map<Long, List<CartItemDTO>> userCarts = new HashMap<>();

    // get cart from userid
    public List<CartItemDTO> getCartByUserId(Long userId) {
        return userCarts.getOrDefault(userId, new ArrayList<>());
    }

    // add product to cart
    public String addToCart(Long userId, CartItemDTO cartItem) {

        //check userid if user null is not customer
        if (userId == null) {
            return "Only customers can have a cart";
        }

        List<CartItemDTO> cart = userCarts.computeIfAbsent(userId, k -> new ArrayList<>());

        // check product
        if (cartItem.getProduct() == null) {
            return "Invalid product";
        }

        ProductDTO product = cartItem.getProduct();

        // check number quantity
        if (cartItem.getQuantity() <= 0) {
            return "Invalid quantity";
        }

        // check if product exist in cart
        for (CartItemDTO item : cart) {
            if (item.getProduct().getProductID().equals(product.getProductID())) {
                int newQuantity = item.getQuantity() + cartItem.getQuantity();

                // check stock quantity
                if (newQuantity > product.getStockQuantity()) {
                    return "Not enough stock available";
                }

                item.setQuantity(newQuantity);
                return "Product quantity updated in cart";
            }
        }

        // check stock quantity
        if (cartItem.getQuantity() > product.getStockQuantity()) {
            return "Not enough stock available";
        }

        // add new product to cart
        cart.add(new CartItemDTO(product, cartItem.getQuantity()));
        return "Added to cart successfully";
    }

    // delete product from cart
    public String removeFromCart(Long userId, Long productId) {

        //check userid if user null is not customer
        if (userId == null) {
            return "Only customers can have a cart";
        }

        List<CartItemDTO> cart = userCarts.get(userId);
        if (cart == null || cart.isEmpty()) {
            return "Cart is empty";
        }

        boolean removed = cart.removeIf(item ->
                item.getProduct() != null && Objects.equals(item.getProduct().getProductID(), productId)
        );

        return removed ? "Product removed from cart" : "Product not found in cart";
    }


    //reduce quantity
    @Override
    public CartItemDTO reduceQuantity(Long userId, Long productId, Integer quantity) {
        List<CartItemDTO> cart = userCarts.get(userId);
        if (cart == null || cart.isEmpty()) {
            return null; // response if cart null
        }

        for (CartItemDTO item : cart) {
            if (Objects.equals(item.getProduct().getProductID(), productId)) {
                if (item.getQuantity() > quantity) {
                    item.setQuantity(item.getQuantity() - quantity); // reduce quantity
                    return item; // response cartItem updated
                } else {
                    cart.remove(item); // remove product from cart ìf quantity <= 0
                    return null; // response null if product is removed from cart
                }
            }
        }

        return null; // response ìf not found product
    }



    // delete all cart
    public void clearCart(Long userId) {
        userCarts.remove(userId);
    }
}
