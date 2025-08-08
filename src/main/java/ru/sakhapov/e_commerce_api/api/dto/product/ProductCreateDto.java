package ru.sakhapov.e_commerce_api.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateDto {

    @NonNull
    String name;

    String description;

    @NonNull
    BigDecimal price;

    @NonNull
    Long quantity;

    @JsonProperty("isAvailable")
    boolean isAvailable;

    Long categoryId;

    Long brandId;
}
