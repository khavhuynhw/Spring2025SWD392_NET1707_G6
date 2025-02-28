package com.net1707.backend.controller;

import com.net1707.backend.dto.CartItemDTO;
import com.net1707.backend.dto.ProductDTO;
import com.net1707.backend.service.Interface.ICartService;
import com.net1707.backend.service.Interface.IProductService;
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
    private final IProductService iProductService;

    public CartController(ICartService iCartService, IProductService iProductService) {
        this.iCartService = iCartService;
        this.iProductService = iProductService;
    }

    // Lấy giỏ hàng từ session, nếu chưa có thì tạo mới
    private List<CartItemDTO> getCart(HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(HttpSession session) {
        List<CartItemDTO> cart = getCart(session);

        if (cart.isEmpty()) {
            return ResponseEntity.noContent().build(); // Trả về 204 nếu giỏ hàng trống
        }

        return ResponseEntity.ok(cart);
    }

    @GetMapping("/item/{productId}")
    public ResponseEntity<CartItemDTO> getCartItem(@PathVariable Long productId, HttpSession session) {
        List<CartItemDTO> cart = getCart(session);
        return cart.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemDTO cartItemDTO, HttpSession session) {
        List<CartItemDTO> cart = getCart(session);

        // Gọi service để thêm sản phẩm vào giỏ hàng
        String response = iCartService.addToCart(cartItemDTO, cart);

        // **Lưu lại giỏ hàng vào session sau khi cập nhật**
        session.setAttribute("cart", cart);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId, HttpSession session) {
        List<CartItemDTO> cart = getCart(session);
        String response = iCartService.removeFromCart(productId, cart);

        // **Cập nhật session sau khi xóa**
        session.setAttribute("cart", cart);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(HttpSession session) {
        session.invalidate(); // Xóa toàn bộ session
        return ResponseEntity.ok("Cart cleared");
    }

//    @ModelAttribute("cart")
//    public List<CartItemDTO> initializeCart() {
//        return new java.util.ArrayList<>();
//    }
//
//    private List<CartItemDTO> getCart(HttpSession session) {
//        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new ArrayList<>();
//            session.setAttribute("cart", cart);
//        }
//        return cart;
//    }
//    @GetMapping("/view")
//    public ResponseEntity<List<CartItemDTO>> viewCart(HttpSession session) {
//        List<CartItemDTO> cart = getCart(session);
//
//        List<CartItemDTO> updatedCart = cart.stream().map(item -> {
//            ProductDTO productDTO = iProductService.getProductById(item.getProductId());
//            return new CartItemDTO(
//                    item.getProductId(),
//                    item.getQuantity()
//            );
//        }).toList();
//
//        return updatedCart.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(updatedCart);
//    }
//
//    @GetMapping("/item/{productId}")
//    public ResponseEntity<CartItemDTO> getCartItem(@PathVariable Long productId, HttpSession session) {
//        List<CartItemDTO> cart = getCart(session);
//        return cart.stream()
//                .filter(item -> item.getProductId().equals(productId))
//                .findFirst()
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addToCart(@RequestBody CartItemDTO cartItemDTO, HttpSession session) {
//        List<CartItemDTO> cart = getCart(session);
//        String response = iCartService.addToCart(cartItemDTO, cart);
//        session.setAttribute("cart", cart); // Cập nhật session
//        return response.equals("Product not found") ? ResponseEntity.badRequest().body(response) : ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/remove/{productId}")
//    public ResponseEntity<String> removeFromCart(@PathVariable Long productId, HttpSession session) {
//        List<CartItemDTO> cart = getCart(session);
//        String response = iCartService.removeFromCart(productId, cart);
//        session.setAttribute("cart", cart);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/clear")
//    public ResponseEntity<String> clearCart(HttpSession session) {
//        session.removeAttribute("cart");
//        return ResponseEntity.ok("Cart cleared");
//    }


}
