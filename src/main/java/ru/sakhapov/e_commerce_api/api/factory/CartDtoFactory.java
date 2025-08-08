package ru.sakhapov.e_commerce_api.api.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sakhapov.e_commerce_api.api.dto.cart.CartDto;
import ru.sakhapov.e_commerce_api.store.entity.Cart;

@Component
@RequiredArgsConstructor
public class CartDtoFactory {

    private final CartItemDtoFactory itemDtoFactory;

    public CartDto makeCartDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(cart.getItems().stream()
                        .map(itemDtoFactory::makeCartItemDto)
                        .toList())

                .build();
    }
}
