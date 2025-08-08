package ru.sakhapov.e_commerce_api.api.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateDto {

    @NonNull
    private Long productId;

    @NonNull
    private Long quantity;
}
