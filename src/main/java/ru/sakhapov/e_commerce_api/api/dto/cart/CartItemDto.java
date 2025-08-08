package ru.sakhapov.e_commerce_api.api.dto.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CartItemDto {

    @NonNull
    Long id;

    Long productId;

    String productName;

    BigDecimal price;

    Long quantity;
}
