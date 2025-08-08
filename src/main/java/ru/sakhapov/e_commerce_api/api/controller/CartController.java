package ru.sakhapov.e_commerce_api.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.e_commerce_api.api.dto.AckDto;
import ru.sakhapov.e_commerce_api.api.dto.cart.AddToCartRequest;
import ru.sakhapov.e_commerce_api.api.dto.cart.CartDto;
import ru.sakhapov.e_commerce_api.store.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @GetMapping
    public CartDto getCart() {
        return cartService.getCart();
    }

    @PostMapping("/add")
    public AckDto addToCart(@RequestBody AddToCartRequest request) {
        cartService.addProductToCart(request);
        return AckDto.makeDefault(true);
    }

    @DeleteMapping("/clear")
    public AckDto clearCart() {
        cartService.clearCart();
        return AckDto.makeDefault(true);
    }
}