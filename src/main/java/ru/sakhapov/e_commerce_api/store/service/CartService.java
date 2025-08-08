package ru.sakhapov.e_commerce_api.store.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sakhapov.e_commerce_api.api.dto.cart.AddToCartRequest;
import ru.sakhapov.e_commerce_api.api.dto.cart.CartDto;
import ru.sakhapov.e_commerce_api.api.exception.NotFoundException;
import ru.sakhapov.e_commerce_api.api.factory.CartDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.Cart;
import ru.sakhapov.e_commerce_api.store.entity.CartItem;
import ru.sakhapov.e_commerce_api.store.entity.Product;
import ru.sakhapov.e_commerce_api.store.entity.User;
import ru.sakhapov.e_commerce_api.store.repository.CartItemRepository;
import ru.sakhapov.e_commerce_api.store.repository.CartRepository;
import ru.sakhapov.e_commerce_api.store.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    CurrentUserService currentUserService;
    CartDtoFactory cartDtoFactory;

    @Transactional
    public void addProductToCart(AddToCartRequest request) {

        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found with id = " + request.getProductId()));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElseGet(() -> {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(0L);
            return newCartItem;
        });

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public CartDto getCart() {
        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart is empty"));

        return cartDtoFactory.makeCartDto(cart);
    }

    @Transactional
    public void clearCart() {
        Cart cart = cartRepository.findByUser(currentUserService.getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Cart is already empty"));

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}