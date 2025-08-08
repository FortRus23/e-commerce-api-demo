package ru.sakhapov.e_commerce_api.api.factory;

import org.springframework.stereotype.Component;
import ru.sakhapov.e_commerce_api.api.dto.cart.CartItemDto;
import ru.sakhapov.e_commerce_api.store.entity.CartItem;
import ru.sakhapov.e_commerce_api.store.entity.Product;

@Component
public class CartItemDtoFactory {

    public CartItemDto makeCartItemDto(CartItem item) {
        Product product = item.getProduct();

        return CartItemDto.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
}