package com.net1707.backend.controller;

import com.net1707.backend.dto.CartItemDTO;
import com.net1707.backend.security.UserDetailsImpl;
import com.net1707.backend.service.Interface.ICartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final ICartService iCartService;

    public CartController(ICartService iCartService) {
        this.iCartService = iCartService;
    }

    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) { // get user from SecurityContext

        Long userId = userDetails.getId();

        List<CartItemDTO> cart = iCartService.getCartByUserId(userId);

        if (cart.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cart);
    }


    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartItemDTO cartItemDTO) {

        System.out.println("Received CartItemDTO: " + cartItemDTO);

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userDetails.getId();

        if (cartItemDTO == null || cartItemDTO.getProduct() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product data");
        }

        String response = iCartService.addToCart(userId, cartItemDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userDetails.getId();
        String response = iCartService.removeFromCart(userId, productId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<CartItemDTO> updateQuantity(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userDetails.getId();
        CartItemDTO updatedCartItem = iCartService.updateQuantity(userId, productId, quantity);

        if (updatedCartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(updatedCartItem);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userDetails.getId();
        iCartService.clearCart(userId);

        return ResponseEntity.ok("Cart cleared successfully");
    }







}
