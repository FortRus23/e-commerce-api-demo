package ru.sakhapov.e_commerce_api.api.dto.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CartDto {

    @NonNull
    Long id;

    Long userId;

    List<CartItemDto> items = new ArrayList<>();
}
