package com.net1707.backend.service;

import com.net1707.backend.dto.CartItemDTO;
import com.net1707.backend.dto.ProductBatchDTO;
import com.net1707.backend.dto.ProductDTO;
import com.net1707.backend.mapper.ProductBatchMapper;
import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.repository.ProductBatchRepository;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.service.Interface.ICartService;
import com.net1707.backend.service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.*;

@Service
public class CartService implements ICartService {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private ProductBatchRepository productBatchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductBatchMapper productBatchMapper;

    // get cart form userid
    private final Map<Long, List<CartItemDTO>> userCarts = new HashMap<>();

    // get cart from userid
    public List<CartItemDTO> getCartByUserId(Long userId) {
        return userCarts.getOrDefault(userId, new ArrayList<>());
    }

    public String addToCart(Long userId, CartItemDTO cartItem) {
        if (userId == null) {
            return "Only customers can have a cart";
        }

        if (cartItem == null || cartItem.getProduct() == null) {
            return "Invalid product";
        }

        if (cartItem.getQuantity() <= 0) {
            return "Invalid quantity";
        }

        ProductDTO product = cartItem.getProduct();
        Product productEntity = productRepository.findById(product.getProductID())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<CartItemDTO> cart = userCarts.computeIfAbsent(userId, k -> new ArrayList<>());

        // get list batch has expired day
        List<ProductBatch> batches = productBatchRepository.findByProduct(productEntity);
        batches.sort(Comparator.comparing(ProductBatch::getExpireDate));

        int availableStock = batches.stream().mapToInt(ProductBatch::getQuantity).sum();
        if (cartItem.getQuantity() > availableStock) {
            return "Not enough stock available";
        }

        // check product has exist in cart
        for (CartItemDTO item : cart) {
            if (item.getProduct().getProductID().equals(product.getProductID())) {
                int newQuantity = item.getQuantity() + cartItem.getQuantity();

                // check batch before add quantity
                if (newQuantity > availableStock) {
                    return "Not enough stock for product " + product.getProductID();
                }

                // update quantity
                item.setQuantity(newQuantity);

                // update batch
                List<ProductBatchDTO> updatedBatches = new ArrayList<>();
                int remainingUpdateQuantity = newQuantity;

                for (ProductBatch batch : batches) {
                    if (remainingUpdateQuantity <= 0) break;

                    int takeQuantity = Math.min(batch.getQuantity(), remainingUpdateQuantity);
                    ProductBatchDTO batchDTO = productBatchMapper.toDto(batch);
                    batchDTO.setQuantity(takeQuantity);
                    updatedBatches.add(batchDTO);

                    remainingUpdateQuantity -= takeQuantity;
                }

                // update list batch
                item.setBatches(updatedBatches);

                return "Product quantity updated in cart";
            }
        }

        // if product does not exist in cart, add new
        List<ProductBatchDTO> selectedBatches = new ArrayList<>();
        int remainingQuantity = cartItem.getQuantity();

        for (ProductBatch batch : batches) {
            if (remainingQuantity <= 0) break;

            int takeQuantity = Math.min(batch.getQuantity(), remainingQuantity);
            ProductBatchDTO batchDTO = productBatchMapper.toDto(batch);
            batchDTO.setQuantity(takeQuantity);
            selectedBatches.add(batchDTO);

            remainingQuantity -= takeQuantity;
        }

        // add product to cart but do not - quantity in batch
        cart.add(new CartItemDTO(product, cartItem.getQuantity(), selectedBatches));
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


    //update quantity
    @Override
    public CartItemDTO updateQuantity(Long userId, Long productId, Integer quantity) {
        List<CartItemDTO> cart = userCarts.get(userId);
        if (cart == null || cart.isEmpty()) {
            return null; // if cart is empty, response null
        }

        for (CartItemDTO item : cart) {
            if (Objects.equals(item.getProduct().getProductID(), productId)) {
                int stockQuantity = item.getProduct().getStockQuantity();
                if (quantity > stockQuantity) {
                    return item; // response old CartItem if quantity > stockQuantity
                }
                if (quantity > 0) {
                    item.setQuantity(quantity); // update new quantity
                    // get list batch has expired date
                    Product productEntity = productRepository.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    List<ProductBatch> batches = productBatchRepository.findByProduct(productEntity);
                    batches.sort(Comparator.comparing(ProductBatch::getExpireDate));
                    // update batch to new quantity
                    List<ProductBatchDTO> updatedBatches = new ArrayList<>();
                    int remainingQuantity = quantity;

                    for (ProductBatch batch : batches) {
                        if (remainingQuantity <= 0) break;

                        int takeQuantity = Math.min(batch.getQuantity(), remainingQuantity);
                        ProductBatchDTO batchDTO = productBatchMapper.toDto(batch);
                        batchDTO.setQuantity(takeQuantity);
                        updatedBatches.add(batchDTO);

                        remainingQuantity -= takeQuantity;
                    }

                    // assign batch for cartItem
                    item.setBatches(updatedBatches);

                    return item; // response cartItem update
                }
            }
        }

        return null;
    }




    // delete all cart
    public void clearCart(Long userId) {
        userCarts.remove(userId);
    }
}
