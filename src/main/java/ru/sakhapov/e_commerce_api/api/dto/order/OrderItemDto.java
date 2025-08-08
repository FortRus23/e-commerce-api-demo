package ru.sakhapov.e_commerce_api.api.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class OrderItemDto {

    @NonNull
    Long id;

    @NonNull
    Long productId;

    String productName;

    Long quantity;

    BigDecimal price;
}
