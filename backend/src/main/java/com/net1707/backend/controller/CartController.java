package com.net1707.backend.controller;

import com.net1707.backend.dto.CartItemDTO;
import com.net1707.backend.service.Interface.ICartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.net1707.backend.security.JwtUtil;
import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final JwtUtil jwtUtil;
    private final ICartService iCartService;

    public CartController(JwtUtil jwtUtil, ICartService iCartService) {
        this.jwtUtil = jwtUtil;
        this.iCartService = iCartService;
    }
    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(
            @RequestHeader("Authorization") String token) { // get token from request

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", "")); // get userId from JWT

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<CartItemDTO> cart = iCartService.getCartByUserId(userId); // get cart from service

        if (cart.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestHeader("Authorization") String token,
            @RequestBody CartItemDTO cartItemDTO) {

        System.out.println("Received CartItemDTO: " + cartItemDTO);

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (cartItemDTO == null || cartItemDTO.getProduct() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product data");
        }

        String response = iCartService.addToCart(userId, cartItemDTO);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId) {

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String response = iCartService.removeFromCart(userId, productId); // call service to remove product

        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{productId}")
    public ResponseEntity<CartItemDTO> updateQuantity(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartItemDTO updatedCartItem = iCartService.updateQuantity(userId, productId, quantity); // call service to update quantity

        if (updatedCartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Not Found
        }

        return ResponseEntity.ok(updatedCartItem);
    }



    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(
            @RequestHeader("Authorization") String token) {

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        iCartService.clearCart(userId); // call service to clear cart

        return ResponseEntity.ok("Cart cleared successfully");
    }







}
