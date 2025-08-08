package ru.sakhapov.e_commerce_api.api.dto.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ProductDto {

    @NonNull
    Long id;

    @NonNull
    private String name;

    private String description;

    @NonNull
    private BigDecimal price;

    @NonNull
    private Long quantity;

    private boolean isAvailable;

    private Long categoryId;

    private Long brandId;
}
