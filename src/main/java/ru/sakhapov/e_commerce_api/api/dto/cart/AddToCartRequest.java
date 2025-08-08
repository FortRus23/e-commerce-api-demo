package ru.sakhapov.e_commerce_api.api.dto.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    Long productId;
    Long quantity;
}