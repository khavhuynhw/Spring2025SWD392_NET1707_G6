package com.net1707.backend.controller;

import com.net1707.backend.dto.CartItemDTO;
import com.net1707.backend.service.Interface.ICartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
@SessionAttributes("cart")
@CrossOrigin(origins = "*")
public class CartController {


    private final ICartService iCartService;

    public CartController(ICartService iCartService) {
        this.iCartService = iCartService;
    }

    @ModelAttribute("cart")
    public List<CartItemDTO> initializeCart() {
        return new java.util.ArrayList<>();
    }

    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        return cart.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cart);
    }

//    @GetMapping("/view")
//    public ResponseEntity<List<CartItemDTO>> viewCart(@ModelAttribute("cart") List<CartItemDTO> cart) {
//        return cart.isEmpty()
//                ? ResponseEntity.noContent().build()
//                : ResponseEntity.ok(cart);
//    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam int productId, @RequestParam int quantity,
                                            @ModelAttribute("cart") List<CartItemDTO> cart) {
        return ResponseEntity.ok(iCartService.addToCart(productId, quantity, cart));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable int productId, @ModelAttribute("cart") List<CartItemDTO> cart) {
        return ResponseEntity.ok(iCartService.removeFromCart(productId, cart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(SessionStatus status) {
        status.setComplete(); // Xóa session
        return ResponseEntity.ok("Cart cleared");
    }
}
